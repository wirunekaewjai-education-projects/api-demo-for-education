<?php

// ถ้า title เป็นค่าว่างหรือไม่ได้ส่งมาจะจบการทำงานทันที
filter_empty_vars($_POST, ['title']);

// อ่านเรื่อง trim ได้ที่ : http://www.w3schools.com/php/func_string_trim.asp
// trim เพื่อลบ white space ซ้ายขวาของสตริง เช่น "  hello world!  " เป็น "hello world!";
$title = trim($_POST['title']);

// อ่านเรื่อง Ternary Operator ()?: ได้ที่ : http://www.sitepoint.com/using-the-ternary-operator/
// ถ้า content ไม่ถูกส่งมาให้กำหนดเป็นสตริงว่าง
$content = empty($_POST['content']) ? '' : trim($_POST['content']);

// อ่านเรื่อง substr ได้ที่ http://www.w3schools.com/php/func_string_substr.asp
// ทำการย่อ content ให้เหลือแค่สั้นๆ ไม่เกิน 64 ตัวอักษร
$excerpt_length = 64;
$excerpt = trim(substr($content, 0, $excerpt_length));

// ถ้า Connect ไม่ได้จะจบการทำงานทันที
$conn = database_connect();

// สร้าง SQL Command สำหรับเพิ่มโพส
$sql = "INSERT INTO `posts`(`title`, `excerpt`, `content`)
        VALUES ('$title', '$excerpt', '$content')";

// ถ้าทำงานผิดพลาดจะจบการทำงานทันที
database_query_boolean($conn, $sql);

// ------- เพิ่มข้อมูลสำเร็จ ลำดับต่อไปคือแสดงผล -------- //

// อ่านเรือง Last Insert ID ได้ที่ http://www.w3schools.com/php/php_mysql_insert_lastid.asp
// อ่านค่าไอดีที่เพิ่มลงในฐานข้อมูลล่าสุด
$inserted_id = $conn->insert_id;

// สร้าง Array ของชื่อฟิลด์ที่อนุญาตให้ใช้ได้
// string 'id' | 'title' | 'excerpt' | 'content' | 'created_date' | 'modified_date'
$field_default = "`id`, `title`, `created_date`";
$fields = parse_fields($_GET, 'fields', $field_default);

$sql = "SELECT $fields
        FROM `posts`
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
