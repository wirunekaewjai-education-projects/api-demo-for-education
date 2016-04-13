<?php

// อ่านเรื่อง require & include ได้ที่ http://www.w3schools.com/php/php_includes.asp
// หรือ http://www.thaiseoboard.com/index.php?topic=70259.0;wap2

// แทรกโค้ดจาก _Helper.dp
require_once('_Helper.php');

// ถ้าไม่ใช่ GET จะจบการทำงานทันที
filter_request('GET');

// ถ้า Connect ไม่ได้จะจบการทำงานทันที
$conn = database_connect();

// อ่านเรื่อง SQL (MySQL) ได้ที่ : http://www.w3schools.com/php/php_mysql_intro.asp
// สร้าง SQL Command สำหรับการอ่านข้อมูล
$sql = "SELECT `id`,`title`,`excerpt`,`created_date`,`modified_date`
        FROM `posts`
        ORDER BY `created_date` DESC";

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

// สร้างตัวแปรอาร์เรย์เพื่อเก็บผลลัพธ์
$array = [];

// ถ้า Query แล้วมีข้อมูลจะต้องได้ num_rows > 0
if($result->num_rows > 0)
{
    // Query แบบ assoc (Associative Array) : http://www.w3schools.com/php/php_mysql_select.asp
    // อ่านเรื่อง Associative Array ได้ที่ : http://www.w3schools.com/php/php_arrays.asp

    // While-Loop : http://www.w3schools.com/php/php_looping.asp
    while($row = $result->fetch_assoc())
    {
        // สร้าง Associative Array เพื่อเก็บข้อมูลในแต่ละ row
        $item = [];
        $item['id'] = (int) $row['id'];
        $item['title'] = $row['title'];
        $item['excerpt'] = $row['excerpt'];
        $item['created_date'] = $row['created_date'];
        $item['modified_date'] = $row['modified_date'];

        // อ่านเรื่องการเพิ่มข้อมูลได้ที่ : http://php.net/manual/ru/function.array-push.php
        // เพิ่มข้อมูลลงในอาร์เรย์
        $array[] = $item;
    }
}

// ปิดการเชื่อมต่อ MySQL
$conn->close();

response_json($array);

?>
