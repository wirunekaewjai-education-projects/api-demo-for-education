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
// อ่านเรื่อง empty ได้ที่ : http://goo.gl/IDKN1i, https://goo.gl/pntS6D และ http://php.net/manual/en/function.empty.php
if(empty($_POST['a']) === true || empty($_POST['b']) === true || empty($_POST['operator']) === true)
{
    // 400 : Bad Request
    http_response_code(400);

    // จบการทำงานของไฟล์ทันทีเพราะมีข้อมูลไม่ครบ
    die();
}

// อ่านเรื่อง is_numeric ได้ที่ http://www.w3resource.com/php/function-reference/is_numeric.php
if(is_numeric($_POST['a']) === false || is_numeric($_POST['b']) === false)
{
    // 400 : Bad Request
    http_response_code(400);

    // จบการทำงานของไฟล์ทันทีเพราะ a และ b ไม่ใช่ตัวเลข
    die();
}

// อ่านเรื่องฟังก์ชัน doubleval() ได้ที่ : http://php.net/manual/en/function.doubleval.php
// แปลงสตริงเป็นเลขทศนิยม
$a = doubleval($_POST['a']);
$b = doubleval($_POST['b']);

$operator = $_POST['operator'];
$result = 0;
if($operator === 'add')
{
    $result = $a + $b;
}
else if($operator === 'subtract')
{
    $result = $a - $b;
}
else if($operator === 'multiply')
{
    $result = $a * $b;
}
else if($operator === 'divide')
{
    $result = $a / $b;
}
else
{
    // 400 : Bad Request
    http_response_code(400);

    // จบการทำงานของไฟล์ทันทีเพราะ $operator ไม่ใช่เครื่องหมาย + - * /
    die();
}

$object =
[
    'a' => $a,
    'b' => $b,
    'operator' => $operator,
    'result' => $result,
    'method' => 'POST'
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
