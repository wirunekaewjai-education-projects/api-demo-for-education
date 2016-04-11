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

// ถ้า Connect ไม่ได้จะจบการทำงานทันที
$conn = database_connect();

// อ่านเรื่อง SQL (MySQL) ได้ที่ : http://www.w3schools.com/php/php_mysql_intro.asp
// สร้าง SQL Command สำหรับการอ่านข้อมูล
$sql = "SELECT `id`,`title`,`excerpt`,`created_date`,`modified_date`
        FROM `posts`
        WHERE `id`=$id
        ORDER BY `created_date` DESC
        LIMIT 1";

/*
    จะสังเกตุเห็นว่าไฟล์ php นี้มี '', "" และ ``
    '' หรือ "" หมายถึงสตริง
    ส่วน `` ใช้ครอบชื่อคอลัมน์หรือชื่อตารางใน MySQL เพื่อป้องกันการเป็น keyword
    สมมติ MySQL กำหนดให้คำว่า title เป็น keyword ใน SQL Command แล้วเราดันสร้างตารางที่มีชื่อคอลัมน์ว่า title
    เวลาจะเขียน SQL Command อาจจะทำให้ MySQL งงจนเพี้ยนได้
    เพราะฉะนั้น MySQL เลยบอกให้เราครอบ `` ไว้เพื่อกำกับว่าเป็นชื่อคอลัมน์หรือฃื่อตารางนั่นเอง MySQL จะได้ไม่สับสน
*/

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

    // อ่านเรื่อง intval ได้ที่ : http://php.net/manual/en/function.intval.php
    $object['id'] = intval($row['id']);
    $object['title'] = $row['title'];
    $object['excerpt'] = $row['excerpt'];
    $object['created_date'] = $row['created_date'];
    $object['modified_date'] = $row['modified_date'];

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
