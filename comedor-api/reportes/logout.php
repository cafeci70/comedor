<?php
require_once __DIR__ . '/auth.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    header('Location: index.php');
    exit;
}

$token = isset($_SESSION['comedor_csrf']) ? (string) $_SESSION['comedor_csrf'] : '';
$enviado = isset($_POST['csrf']) ? (string) $_POST['csrf'] : '';
if ($token === '' || !hash_equals($token, $enviado)) {
    header('Location: index.php');
    exit;
}

$_SESSION = array();

if (ini_get('session.use_cookies')) {
    $params = session_get_cookie_params();
    setcookie(
        session_name(),
        '',
        time() - 42000,
        $params['path'],
        $params['domain'],
        $params['secure'],
        $params['httponly']
    );
}

session_destroy();
header('Location: index.php');
exit;
