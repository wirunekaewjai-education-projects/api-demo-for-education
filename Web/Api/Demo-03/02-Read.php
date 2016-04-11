<?php

// อ่านเรื่อง HTTP GET ได้ที่ http://www.w3schools.com/tags/ref_httpmethods.asp
// อ่านเรื่องตัวแปร $_xxx ได้ที่ : http://www.w3schools.com/php/php_superglobals.asp
// อ่านเรื่องเครื่องหมาย !== ได้ที่ : http://www.w3schools.com/php/php_operators.asp
if($_SERVER['REQUEST_METHOD'] !== 'GET')
{
    // คำอธิบาย HTTP Response Code : https://goo.gl/MXVo6k
    // คำอธิบายฟังก์ชัน http_response_code : http://php.net/manual/en/function.http-response-code.php

    // 405 : Method Not Allowed
    http_response_code(405);

    // อ่านเรื่องฟังก์ชัน die() ได้ที่ : http://www.w3schools.com/php/func_misc_die.asp
    // จบการทำงานของไฟล์ทันทีเพราะรูปแบบรีเควสไม่ใช่ GET
    die();
}

// ถ้าต้องการนำไปใช้กับเซิฟเวอร์ตัวเองให้แก้ข้อมูลให้ตรงด้วย
$server     = 'localhost';
$database   = 'simple_post_db';
$username   = 'root';
$password   = '';

// เชื่อมต่อฐานข้อมูล MySQL
// อ่านเพิ่มเติมได้ที่ : http://www.w3schools.com/php/php_mysql_connect.asp
$conn = new mysqli($server, $username, $password, $database);

// ตรวจสอบการเชื่อมต่อ
if ($conn->connect_error)
{
    // คำอธิบาย HTTP Response Code : https://goo.gl/MXVo6k
    // คำอธิบายฟังก์ชัน : http://php.net/manual/en/function.http-response-code.php

    // 500 : Internal Server Error
    http_response_code(500);

    // จบการทำงานของไฟล์ทันที
    die();
}

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
        // อ่านเรื่อง intval ได้ที่ : http://php.net/manual/en/function.intval.php

        // สร้าง Associative Array เพื่อเก็บข้อมูลในแต่ละ row
        $item = [];
        $item['id'] = intval($row['id']);
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

// อ่านเรื่อง header ได้ที่ : http://php.net/manual/en/function.header.php
// กำหนดให้ข้อมูลที่จะแสดงผลเป็น JSON
header('Content-type: application/json');

// อ่านเรื่องฟังก์ชัน echo ได้ที่ : http://www.w3schools.com/php/func_string_echo.asp
// อ่านเรื่องฟังก์ชัน json_encode ได้ที่ : http://php.net/manual/en/function.json-encode.php
// แปลงข้อมูลในตัวแปร $array เป็น JSON ด้วยฟังก์ชัน json_encode
echo json_encode($array);

// 200 : OK
http_response_code(200);

?>
