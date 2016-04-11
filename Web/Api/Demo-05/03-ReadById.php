<?php

// อ่านเรื่อง require & include ได้ที่ http://www.w3schools.com/php/php_includes.asp
// หรือ http://www.thaiseoboard.com/index.php?topic=70259.0;wap2

// แทรกโค้ดจาก _Helper.dp
require_once('_Helper.php');

// ถ้าไม่ใช่ GET จะจบการทำงานทันที
filter_request('GET');

// ถ้า id เป็นค่าว่างหรือไม่ได้ส่งมาจะจบการทำงานทันที
filter_empty_vars($_GET, ['id']);

// ถ้า id ไม่ใช่ตัวเลขให้จบการทำงานทันที
filter_numeric_vars($_GET, ['id']);

// อ่านค่าไอดีและเก็บไว้ที่ $id
$id = $_GET['id'];

// สร้าง Array ของชื่อฟิลด์ที่อนุญาตให้ใช้ได้
// string 'id' | 'title' | 'excerpt' | 'content' | 'created_date' | 'modified_date'
$field_availables = [ 'id', 'title', 'excerpt', 'content', 'created_date', 'modified_date' ];
$field_default = [ 'id', 'title', 'content', 'created_date', 'modified_date' ];

$fields = parse_string_array_with_backticks($_GET, 'fields', $field_default, $field_availables);

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

// ถ้า Query แล้วมีข้อมูลจะต้องได้ num_rows > 0
if($result->num_rows > 0)
{
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
}
else
{
    // ปิดการเชื่อมต่อ MySQL
    $conn->close();

    // 404 : Not Found
    http_response_code(404);
}


?>
