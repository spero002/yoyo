<?php
require_once dirname(__FILE__) . '/config.php';

class ApiException extends Exception{
}

class ApiConnect{
    public function _verfy_ac($private_key, $params){
        ksort($params);
        $params_data = "";

        foreach($params as $key => $value) {
            $params_data .= $key;
            $params_data .= $value;
        }

        $params_data .= $private_key;
        return sha1($params_data);
    }

    public function get($url, $params, $timeout = 5) {
        if ($url == "" || $timeout <=0) {
            return false;
        }
        $url = $url . '/' . '?' . http_build_query($params);

        echo $url."\n";
        $con = curl_init((string)$url);
        curl_setopt($con, CURLOPT_HEADER, false);
        curl_setopt($con, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($con, CURLOPT_TIMEOUT, (int)$timeout);

        return curl_exec($con);
    }
}

class ApiClient{
    public function init($base_url, $public_key, $private_key) {
        $this->url = $base_url;
        $this->params['PublicKey'] = $public_key;
        $this->private_key = $private_key;
        $this->conn =new ApiConnect();
    }

    public function get($params) {
        foreach($params as $key => $value) {
            $this->params[$key] = $value;
        }
        ksort($this->params);
        $this->params['Signature'] = $this->conn->_verfy_ac($this->private_key, $this->params);
        return $this->conn->get($this->url, $this->params, 5); 
    }
}
?>
