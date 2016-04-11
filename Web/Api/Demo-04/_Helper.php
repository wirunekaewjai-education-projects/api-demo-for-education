<?php

// อ่านเรื่องการใช้งานฟังก์ชันภาษา PHP ได้ที่ : http://www.w3schools.com/php/php_functions.asp
function filter_request($allowed_method)
{
    // อ่านเรื่องตัวแปร $_xxx ได้ที่ : http://www.w3schools.com/php/php_superglobals.asp
    // อ่านเรื่องเครื่องหมาย !== ได้ที่ : http://www.w3schools.com/php/php_operators.asp
    if($_SERVER['REQUEST_METHOD'] !== $allowed_method)
    {
        // คำอธิบาย HTTP Response Code : https://goo.gl/MXVo6k
        // คำอธิบายฟังก์ชัน http_response_code : http://php.net/manual/en/function.http-response-code.php

        // 405 : Method Not Allowed
        http_response_code(405);

        // อ่านเรื่องฟังก์ชัน die() ได้ที่ : http://www.w3schools.com/php/func_misc_die.asp
        // จบการทำงานของไฟล์ทันทีเพราะรูปแบบรีเควสไม่ใช่ที่ต้องการ
        die();
    }
}

// $array คืออาร์เรย์ของข้อมูล เช่น $_GET, $_POST, $_SERVER หรือสร้างเองก็ได้
function filter_empty_vars($array, $keys)
{
    // For-Loop : http://www.w3schools.com/php/php_looping_for.asp
    foreach ($keys as $key)
    {
        // เรื่อง empty อ่านเพิ่มได้ที่ : http://goo.gl/IDKN1i, https://goo.gl/pntS6D
        // และ http://php.net/manual/en/function.empty.php
        if(empty($array[$key]) === true)
        {
            // 400 : Bad Request
            http_response_code(400);

            // จบการทำงานของไฟล์ทันทีเพราะข้อมูลส่งมาไม่ครบตามที่ต้องการ
            die();
        }
    }
}

// $array คืออาร์เรย์ของข้อมูล เช่น $_GET, $_POST, $_SERVER หรือสร้างเองก็ได้
function filter_numeric_vars($array, $keys)
{
    // For-Loop : http://www.w3schools.com/php/php_looping_for.asp
    foreach ($keys as $key)
    {
        // อ่านเรื่อง is_numeric ได้ที่ http://www.w3resource.com/php/function-reference/is_numeric.php
        if(is_numeric($array[$key]) === false)
        {
            // 400 : Bad Request
            http_response_code(400);

            // จบการทำงานของไฟล์ทันทีเพราะข้อมูลส่งมาไม่ตรงฟอร์แมต
            die();
        }
    }
}

function database_connect()
{
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
        http_response_code(500);

        // จบการทำงานของไฟล์ทันที
        die();
    }

    return $conn;
}

function database_query_boolean($conn, $sql)
{
    // รันคำสั่ง SQL
    $result = $conn->query($sql);

    if($result !== TRUE)
    {
        // ปิดการเชื่อมต่อ MySQL
        $conn->close();

        // 400 : Bad Request
        http_response_code(400);
        die();
    }
}

// $response_code จะส่งหรือไม่ส่งค่ามาก็ได้ ถ้าไม่ส่งมาจะเป็น 200 โดยอัตโนมัติ (เรียกว่า Default Argument Value)
// อ่านเรื่อง Default Argument Value ได้ที่ http://php.net/manual/en/functions.arguments.php
function response_json($object, $response_code = 200)
{
    // อ่านเรื่อง header ได้ที่ : http://php.net/manual/en/function.header.php
    // กำหนดให้ข้อมูลที่จะแสดงผลเป็น JSON
    header('Content-type: application/json');

    // อ่านเรื่องฟังก์ชัน echo ได้ที่ : http://www.w3schools.com/php/func_string_echo.asp
    // อ่านเรื่องฟังก์ชัน json_encode ได้ที่ : http://php.net/manual/en/function.json-encode.php
    // แปลงข้อมูลในตัวแปร $object เป็น JSON ด้วยฟังก์ชัน json_encode
    echo json_encode($object);

    http_response_code($response_code);
}

?>
