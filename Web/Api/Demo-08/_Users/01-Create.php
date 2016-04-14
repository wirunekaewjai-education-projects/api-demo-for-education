<?php

// ถ้า title เป็นค่าว่างหรือไม่ได้ส่งมาจะจบการทำงานทันที
filter_empty_vars($_POST, ['username', 'password', 'email']);

// ตรวจสอบ email ว่า format เป็น email จริงหรือไม่
filter_validation_vars($_POST['email'], FILTER_VALIDATE_EMAIL);

$username = $_POST['username'];
$password = $_POST['password'];
$email = $_POST['email'];

// แปลง password ให้อยู่ในรูปแบบ sha256 hash ก่อนเก็บลงฐานข้อมูลเพื่อไม่ให้อ่านรู้เรื่อง
$hash_password = hash('sha256', $password);

// ถ้า Connect ไม่ได้จะจบการทำงานทันที
$conn = database_connect();

// สร้าง SQL Command สำหรับเพิ่มโพส
$sql = "INSERT INTO `users`(`username`, `password`, `email`)
        VALUES ('$username', '$hash_password', '$email')";

// ถ้าทำงานผิดพลาดจะจบการทำงานทันที
database_query_boolean($conn, $sql);

// ------- เพิ่มข้อมูลสำเร็จ ลำดับต่อไปคือแสดงผล -------- //

// อ่านเรือง Last Insert ID ได้ที่ http://www.w3schools.com/php/php_mysql_insert_lastid.asp
// อ่านค่าไอดีที่เพิ่มลงในฐานข้อมูลล่าสุด
$inserted_id = $conn->insert_id;

// สร้าง Array ของชื่อฟิลด์ที่อนุญาตให้ใช้ได้
// string 'id' | 'username' | 'email' | 'created_date'
$field_default = "`id`, `created_date`";
$fields = parse_fields($_GET, 'fields', $field_default);

$sql = "SELECT $fields
        FROM `users`
        WHERE `id`=$inserted_id
        LIMIT 1";

// รันคำสั่ง SQL
$result = $conn->query($sql);

// สร้างตัวแปรแบบ Associative Array เพื่อเก็บผลลัพธ์
$object = [];

// ถ้า Query แล้วมีข้อมูลจะต้องได้ num_rows > 0
if($result->num_rows > 0)
{
    // Query แบบ assoc (Associative Array) : http://www.w3schools.com/php/php_mysql_select.asp
    // อ่านเรื่อง Associative Array ได้ที่ : http://www.w3schools.com/php/php_arrays.asp
    $row = $result->fetch_assoc();

    foreach ($row as $key => $value)
    {
        if($key === 'id')
        {
            $object[$key] = (int) $value;
        }
        else
        {
            $object[$key] = $value;
        }
    }
}

// ปิดการเชื่อมต่อ MySQL
$conn->close();

// 201 : OK - Created
response_json($object, 201);

?>
