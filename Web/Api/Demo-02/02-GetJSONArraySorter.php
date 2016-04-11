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

// อ่านเรื่องตัวแปร $_xxx ได้ที่ : http://www.w3schools.com/php/php_superglobals.asp
// อ่านเรื่อง Associative Array ได้ที่ : http://www.w3schools.com/php/php_arrays.asp
// อ่านเรื่อง empty ได้ที่ : http://goo.gl/IDKN1i, https://goo.gl/pntS6D และ http://php.net/manual/en/function.empty.php
if(empty($_GET['items']) === true)
{
    // 400 : Bad Request
    http_response_code(400);

    // จบการทำงานของไฟล์ทันทีเพราะมีข้อมูลไม่ครบ
    die();
}

// อ่านเรื่อง explode ได้ที่ : http://php.net/manual/en/function.explode.php
// ตัดสตริงแล้วแปลงเป็นอาร์เรย์ด้วย ',' => ตัวอย่าง "a,b,c,d,e" กลายเป็น ["a","b","c","d","e"];
$array = explode(',', $_GET['items']);

// อ่านเรื่อง sort ได้ที่ : http://www.w3schools.com/php/php_arrays_sort.asp
sort($array);

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
