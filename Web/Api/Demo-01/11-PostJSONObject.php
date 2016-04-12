<?php

// อ่านเรื่อง HTTP POST ได้ที่ http://www.w3schools.com/tags/ref_httpmethods.asp
// อ่านเรื่องตัวแปร $_xxx ได้ที่ : http://www.w3schools.com/php/php_superglobals.asp
// อ่านเรื่องเครื่องหมาย !== ได้ที่ : http://www.w3schools.com/php/php_operators.asp
if($_SERVER['REQUEST_METHOD'] !== 'POST')
{
    // คำอธิบาย HTTP Response Code : https://goo.gl/MXVo6k
    // คำอธิบายฟังก์ชัน http_response_code : http://php.net/manual/en/function.http-response-code.php

    // 405 : Method Not Allowed
    http_response_code(405);

    // อ่านเรื่องฟังก์ชัน die() ได้ที่ : http://www.w3schools.com/php/func_misc_die.asp
    // จบการทำงานของไฟล์ทันทีเพราะรูปแบบรีเควสไม่ใช่ POST
    die();
}

// อ่านเรื่อง Associative Array ได้ที่ : http://www.w3schools.com/php/php_arrays.asp
$object =
[
    'method'  => 'POST',
    'message' => 'Hello World!'
];

// อ่านเรื่อง header ได้ที่ : http://php.net/manual/en/function.header.php
// กำหนดให้ข้อมูลที่จะแสดงผลเป็น JSON
header('Content-type: application/json');

// อ่านเรื่องฟังก์ชัน echo ได้ที่ : http://www.w3schools.com/php/func_string_echo.asp
// อ่านเรื่องฟังก์ชัน json_encode ได้ที่ : http://php.net/manual/en/function.json-encode.php
// แปลงข้อมูลในตัวแปร $object เป็น JSON ด้วยฟังก์ชัน json_encode
echo json_encode($object);

// 200 : OK
http_response_code(200);

?>
