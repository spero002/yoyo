<?php
require_once dirname(__FILE__) . '/config.php';

class ApiException extends Exception {
    function __construct() {
        return "Request api Error!";
    }   
}

function _verfy_ac($private_key, $params) {
    ksort($params);
    $params_data="";

    foreach($params as $key => $value) {
        $params_data .= $key;
        $params_data .= $value;
    }

    $params_data .= $private_key;
    return sha1($params_data);
}

class ApiConnection{
    function __construct() {
    }

    function __destruct() {
        curl_close($this->conn);
    }

    function get($url, $params, $timeout = 5) {
        if ($url == "" || $timeout <= 0) {
            return false;
        }
        $this->url = $url . '?' . http_build_query($params);
        $this->conn = curl_init((string)$this->url);
        curl_setopt($this->conn, CURLOPT_HEADER, false);
        curl_setopt($this->conn, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($this->conn, CURLOPT_TIMEOUT, (int)$timeout);
        curl_exec($this->conn);
        $this->response = curl_getinfo($this->conn);
        echo $this->response;       
    }

}

class ApiClient{
    function __construct($public_key, $private_key) {
        $this->g_params = array();
        $this->g_params['PublicKey'] = $public_key;
        $this->private_key = $private_key;
        $this->conn = new ApiConnection();
    }

    public function get($uri, $params) {
        $_params = $params;
        $_params["Signature"] = _verfy_ac($this->private_key, $_params);
        return conn.get($url, $_params);
    }
}

?>
