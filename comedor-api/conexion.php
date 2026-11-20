<?php

/**
 * Database connection wrapper using environment overrides with local dev defaults.
 */
class conexion
{
    /** @var mysqli|null */
    private $conn;

    public function __construct()
    {
        $host = getenv('DB_HOST') !== false ? getenv('DB_HOST') : 'localhost';
        $user = getenv('DB_USER') !== false ? getenv('DB_USER') : 'root';
        $pass = getenv('DB_PASS') !== false ? getenv('DB_PASS') : '';
        $db   = getenv('DB_NAME') !== false ? getenv('DB_NAME') : 'comedor';

        mysqli_report(MYSQLI_REPORT_OFF);
        $this->conn = @new mysqli($host, $user, $pass, $db);

        if ($this->conn && !$this->conn->connect_errno) {
            if (!$this->conn->set_charset('utf8mb4')) {
                $this->conn->set_charset('utf8');
            }
        } elseif ($this->conn) {
            error_log('MySQL connection error (' . $this->conn->connect_errno . '): ' . $this->conn->connect_error);
        } else {
            error_log('MySQL connection error: unable to instantiate mysqli');
        }
    }

    /**
     * @return mysqli|null
     */
    public function get_conexion()
    {
        return $this->conn;
    }
}
