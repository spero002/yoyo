<?php
require_once dirname(__FILE__) . '/../utils.php';
require_once dirname(__FILE__) . '/../config.php';

$ApiClient = new ApiClient();
$ApiClient->init(BASE_URL, PUBLIC_KEY, PRIVATE_KEY);

$Parameters = array('Action' => 'DescribeUDBBackup',
    'Region' => 'cn-north-01',
    'Offset' => 0,
    'Limit' => 20
);
$response = $ApiClient->get($Parameters);
echo $response . "\n";

?>
