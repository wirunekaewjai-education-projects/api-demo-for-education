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

// อ่านเรื่องตัวแปร $_xxx ได้ที่ : http://www.w3schools.com/php/php_superglobals.asp
// อ่านเรื่อง Associative Array ได้ที่ : http://www.w3schools.com/php/php_arrays.asp
$a = $_POST['a'];
$b = $_POST['b'];

// อ่านเรื่องฟังก์ชัน echo ได้ที่ : http://www.w3schools.com/php/func_string_echo.asp
echo 'a: ' . $a . ', b: ' . $b;

// 200 : OK
http_response_code(200);

?>
