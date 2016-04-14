<?php

// Basic Authentication
// อ่านเพิ่มเติมได้ที่ : https://en.wikipedia.org/wiki/Basic_access_authentication
// หรือ http://php.net/manual/en/features.http-auth.php
function filter_basic_auth()
{
    // เรื่องตัวแปร $_xxx อ่านเพิ่มได้ที่ : http://www.w3schools.com/php/php_superglobals.asp
    // เรื่อง empty อ่านเพิ่มได้ที่ : http://goo.gl/IDKN1i, https://goo.gl/pntS6D
    // และ http://php.net/manual/en/function.empty.php

    // ตรวจสอบว่ามีข้อมูลส่งมาหรือไม่
    $b1 = empty($_SERVER['PHP_AUTH_USER']);
    $b2 = empty($_SERVER['PHP_AUTH_PW']);

    if($b1 || $b2)
    {
        // คำอธิบาย HTTP Response Code : https://goo.gl/MXVo6k
        // คำอธิบายฟังก์ชัน : http://php.net/manual/en/function.http-response-code.php

        // 401 : Unauthorized
        http_response_code(400);

        // จบการทำงานของไฟล์ทันทีเพราะข้อมูลส่งมาไม่ครบตามที่ต้องการ
        die();
    }

    // เรื่องตัวแปร $_xxx อ่านเพิ่มได้ที่ : http://www.w3schools.com/php/php_superglobals.asp
    $username = $_SERVER['PHP_AUTH_USER'];

    // http://php.net/manual/en/function.hash.php
    // แปลง password ให้อยู่ในรูปแบบ sha256 hash ก่อนเก็บลงฐานข้อมูลเพื่อไม่ให้อ่านรู้เรื่อง
    $password = hash('sha256', $_SERVER['PHP_AUTH_PW']);

    // สร้าง SQL Command สำหรับตรวจสอบ Username, Password
    $sql = "SELECT `id`
            FROM `users`
            WHERE `username`='$username' AND `password`='$password'
            LIMIT 1";

    $conn = database_connect();

    // รันคำสั่ง SQL
    $result = $conn->query($sql);

    // ถ้าข้อมูลถูกต้องจะต้องมากกว่า 0
    if($result->num_rows == 0)
    {
        // ปิดการเชื่อมต่อ MySQL
        $conn->close();

        // 401 : Unauthorized
        http_response_code(401);

        // จบการทำงานของไฟล์ทันทีเพราะ Login ไม่ผ่าน
        die();
    }

    // ปิดการเชื่อมต่อ MySQL
    $conn->close();
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

function filter_validation_vars($var, $php_validation_filter)
{
    // อ่านเรื่อง filter_var ได้ที่ : http://php.net/manual/en/filter.examples.validation.php
    if(filter_var($var, $php_validation_filter) === FALSE)
    {
        // 400 : Bad Request
        http_response_code(400);

        // จบการทำงานของไฟล์ทันทีเพราะข้อมูลมีฟอร์แมตไม่ถูกต้อง
        die();
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

function parse_int($array, $key, $default_value, $min, $max = null)
{
    if(empty($array[$key]) === false)
    {
        if(is_numeric($array[$key]) === false)
        {
            // 400 : Bad Request
            http_response_code(400);
            die();
        }

        $val = intval($array[$key]);
        if($val < $min || ($max !== null && $val > $max))
        {
            http_response_code(400);
            die();
        }

        return $val;
    }

    return $default_value;
}

function parse_string($array, $key, $default_value, $availables)
{
    if(empty($array[$key]) === false)
    {
        $val = $array[$key];

        // อ่านเรื่อง in_array ได้ที่ http://php.net/manual/en/function.in-array.php
        if(in_array($val, $availables) === false)
        {
            http_response_code(400);
            die();
        }

        return $val;
    }

    return $default_value;
}

function parse_fields($array, $key, $default_value)
{
    if(empty($array[$key]) === false)
    {
        // อ่านเรื่อง str_replace ได้ที่ : http://www.w3schools.com/php/func_string_str_replace.asp
        $replaced = str_replace(',', '`,`', $array[$key]);
        $replaced = '`' . $replaced . '`';

        // ตัวอย่าง จาก id,title,excerpt เป็น `id`,`title`,`excerpt`
        return $replaced;
    }

    return $default_value;
}


function parse_fields_to_array($array, $key, $default_value)
{
    if(empty($array[$key]) === false)
    {
        // อ่านเรื่อง explode ได้ที่ http://php.net/manual/en/function.explode.php
        // ตัดสตริงด้วย ',' => ตัวอย่าง "id,title,excerpt" กลายเป็น ["id","title","excerpt"];
        return explode(',', $array[$key]);
    }

    return $default_value;
}

?>
