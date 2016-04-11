<?php

// อ่านค่าไอดีและเก็บไว้ที่ $id
$id = $_GET['id'];

// สร้าง Array ของชื่อฟิลด์ที่อนุญาตให้ใช้ได้
// string 'id' | 'title' | 'excerpt' | 'content' | 'created_date' | 'modified_date'
$field_default = "`id`, `title`, `content`, `created_date`";
$fields = parse_fields($_GET, 'fields', $field_default);

// ถ้า Connect ไม่ได้จะจบการทำงานทันที
$conn = database_connect();

// อ่านเรื่อง SQL (MySQL) ได้ที่ : http://www.w3schools.com/php/php_mysql_intro.asp
// สร้าง SQL Command สำหรับการอ่านข้อมูล
$sql = "SELECT $fields
        FROM `posts`
        WHERE `id`=$id
        LIMIT 1";

// รันคำสั่ง SQL
$result = $conn->query($sql);

if($result === false)
{
    // ปิดการเชื่อมต่อ MySQL
    $conn->close();

    // 400 : Bad Request
    http_response_code(400);
    die();
}

if($result->num_rows == 0)
{
    // ปิดการเชื่อมต่อ MySQL
    $conn->close();

    // 404 : Not Found
    http_response_code(404);
    die();
}

// ถ้า Query แล้วมีข้อมูลจะต้องได้ num_rows > 0

// Query แบบ assoc (Associative Array) : http://www.w3schools.com/php/php_mysql_select.asp
// อ่านเรื่อง Associative Array ได้ที่ : http://www.w3schools.com/php/php_arrays.asp
$row = $result->fetch_assoc();

// สร้างตัวแปรแบบ Associative Array เพื่อเก็บผลลัพธ์
$object = [];

foreach ($row as $key => $value)
{
    if($key === 'id')
    {
        // อ่านเรื่อง intval ได้ที่ : http://php.net/manual/en/function.intval.php
        $object[$key] = intval($value);
    }
    else
    {
        $object[$key] = $value;
    }
}

// ปิดการเชื่อมต่อ MySQL
$conn->close();

response_json($object);

?>
