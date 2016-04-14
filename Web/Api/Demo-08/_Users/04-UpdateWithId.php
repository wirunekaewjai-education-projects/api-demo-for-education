<?php

// Login ก่อนทำงาน
filter_basic_auth();

// อ่านค่าไอดีและเก็บไว้ที่ $id
$id = $_GET['id'];

// อ่านเรื่อง file_get_contents ได้ที่ : http://php.net/manual/en/function.file-get-contents.php
// และ http://stackoverflow.com/questions/6805570/how-do-i-access-php-rest-api-put-data-on-the-server-side
$json = file_get_contents("php://input");

// อ่านเรื่อง json_decode ได้ที่ : http://php.net/json_decode
// อ่านข้อมูล JSON ที่ส่งมาแล้วแปลงเป็น Associative Array (true ในพารามิเตอร์หมายถึงให้แปลงเป็น Associative)
$fields = json_decode($json, true);

// ถ้าไม่มีฟิลด์ส่งมาคือยกเลิก
if($fields === null || empty($fields) === true)
{
    // 400 : Bad Request
    http_response_code(400);
    die();
}

$set_array = [];

// อ่านเรื่อง array_key_exists ได้ที่ : http://www.w3schools.com/php/func_array_key_exists.asp
if(array_key_exists('email', $fields) === true)
{
    // อ่านเรื่อง trim ได้ที่ : http://www.w3schools.com/php/func_string_trim.asp
    // trim เพื่อลบ white space ซ้ายขวาของสตริง เช่น "  hello world!  " เป็น "hello world!";
    $text = trim($fields['email']);
    $set_array[] = "`email`='$text'";
}

$password_where_sql = '';
if(array_key_exists('new_password', $fields) === true)
{
    if(empty($fields['current_password']) === true ||
        empty($fields['new_password']) === true)
    {
        // 400 : Bad Request
        http_response_code(400);
        die();
    }

    // แปลง passwrod เก่าและใหม่เป็น sha256 hash
    $hash_new_password = hash('sha256', $fields['new_password']);
    $hash_current_password = hash('sha256', $fields['current_password']);

    // สร้าง sql command
    $set_array[] = "`password`='$hash_new_password'";
    $password_where_sql ="AND `password`='$hash_current_password'";
}


// อ่านเรื่อง implode ได้ที่ http://php.net/manual/en/function.implode.php
// ต่อสตริงจากสตริงอาร์เรย์ด้วย ', '
$set_sql = implode(', ', $set_array);

// ถ้า Connect ไม่ได้จะจบการทำงานทันที
$conn = database_connect();

// สร้าง SQL Command สำหรับอัพเดท
$sql = "UPDATE `users`
        SET $set_sql
        WHERE `id`=$id $password_where_sql";

// die();
// ถ้าทำงานผิดพลาดจะจบการทำงานทันที
database_query_boolean($conn, $sql);

// ตรวจสอบว่าอัพเดทได้จริงหรือไม่ ?
// ถ้าได้จะต้องมีค่ามากกว่า 0 (ในที่นี้ควรจะไม่เกิน 1 เพราะเราอัพเดทแค่ id เดียว)
if($conn->affected_rows > 0)
{
    // 200 : OK
    http_response_code(200);
}
else
{
    // 404 : Not Found
    http_response_code(404);
}

// ปิดการเชื่อมต่อ MySQL
$conn->close();

?>
