<?php

// อ่านเรื่อง require & include ได้ที่ http://www.w3schools.com/php/php_includes.asp
// หรือ http://www.thaiseoboard.com/index.php?topic=70259.0;wap2

// แทรกโค้ดจาก _Helper.dp
require_once('_Helper.php');

/*
    Create  : POST
    Read    : GET
    Update  : PUT
    Delete  : DELETE
*/

$has_id = false;

// เรื่อง empty อ่านเพิ่มได้ที่ : http://goo.gl/IDKN1i, https://goo.gl/pntS6D
// และ http://php.net/manual/en/function.empty.php
if(empty($_GET['id']) === false)
{
    // อ่านเรื่อง is_numeric ได้ที่ http://www.w3resource.com/php/function-reference/is_numeric.php
    if(is_numeric($_GET['id']) === false)
    {
        // คำอธิบาย HTTP Response Code : https://goo.gl/MXVo6k
        // คำอธิบายฟังก์ชัน http_response_code : http://php.net/manual/en/function.http-response-code.php

        // 400 : Bad Request
        http_response_code(400);

        // อ่านเรื่องฟังก์ชัน die() ได้ที่ : http://www.w3schools.com/php/func_misc_die.asp
        // จบการทำงานของไฟล์ทันที
        die();

    }

    $has_id = true;
}

// อ่านเรื่องตัวแปร $_xxx ได้ที่ : http://www.w3schools.com/php/php_superglobals.asp
// อ่านเรื่องเครื่องหมาย === ได้ที่ : http://www.w3schools.com/php/php_operators.asp
if($_SERVER['REQUEST_METHOD'] === 'POST')
{
    if($has_id === false)
    {
        require_once('_Images/01-Create.php');
        die();
    }
}
else if($_SERVER['REQUEST_METHOD'] === 'GET')
{
    if($has_id === false)
    {
        require_once('_Images/02-Read.php');
        die();
    }
    else
    {
        require_once('_Images/03-ReadWithId.php');
        die();
    }
}
else if($_SERVER['REQUEST_METHOD'] === 'DELETE')
{
    if($has_id === true)
    {
        require_once('_Images/04-DeleteWithId.php');
        die();
    }
}

// คำอธิบาย HTTP Response Code : https://goo.gl/MXVo6k
// คำอธิบายฟังก์ชัน http_response_code : http://php.net/manual/en/function.http-response-code.php

// 405 : Method Not Allowed
http_response_code(405);

// อ่านเรื่องฟังก์ชัน die() ได้ที่ : http://www.w3schools.com/php/func_misc_die.asp
// จบการทำงานของไฟล์ทันทีเพราะรูปแบบรีเควสไม่ใช่ที่ต้องการ
die();


?>
