define tomtopweb::instance (
	$instance_name  = "tomtopweb",
	$repo           = "/deploy",
	$port           = 9000,
	$service_ensure = true,
	$service_enable = true,

	$mem = "8192",

	$dbhost = "192.168.56.103",
	$dbuser = "tomtop",
	$dbpass = "tomtop",
	$dbpoolsize = 5,

	$session_host = '127.0.0.1:6379',
	$session_db   = 0,

	$livechat_host = "$session_host",
	$livechat_db   = 1,

	$mobile_redis_host = "$session_host",
	$mobile_redis_db = 2,

	$es_host = "192.168.56.103:9300",

	$facebook_appid = "413569522142784",
	$facebook_appsecret = "b0905391b17848e6ff5a3f6805660db1",

	$paypal_sandbox = true,
	$paypal_clientid = "AZkbeBC4FvWxBGVpADAGoLRty-Hv2of9Ra9ehXogi-A9q6tAu6_78Dlub8Q8",
	$paypal_secret = "EG6pGhD6KnZBp7weV-1eLk7pjgZP-Bp8SykUyJicbxT0cQ3iXeJvVS1s5v6x",
	$paypal_return="http://staging.tomtop.com/",
	$paypal_notifyurl="http://staging.tomtop.com/paypal/payment",

	$oceanpayment_sandbox=true,
	$productApi_token="test",

	$google_clientid = "555482765454-empiejp5kkk80g58ma83k35m2phe7tm8.apps.googleusercontent.com",
	$google_clientsecret = "pqhhsFbtKGAe5keG9C2BSSbE",

	$twitter_key = "2yct1smjyWiY2c7fCeOUn2MlJ",
	$twitter_secret = "kN3TO1irRYKVziWS5ybjnp4XpWlD3yUwy5hH8QxgCupUTVueCw",

	$yahoo_appkey = "13263",
	$yahoo_sign = "6696acc5e1df54d584365ca4844bf76d",

	$rspread_url = "http://service.reasonablespread.com/",
	$rspread_account = "sales160@tomtop.com",
	$rspread_password = "Tt1428.EDM",

	$addthis_pubid = "ra-5540981e730479cb",

	$db_reset_allowed = false,
	
	$order_step = "old",

	$robots_user_agent="*",
	
	$tomtopweb_log_dir = "/var/log/$instance_name",
) {

	file { "$repo":
		ensure => "directory",
	}
	exec { "service $instance_name stop || echo Failed Stopping $instance_name, Ignoring...":
		path    => ["/usr/bin", "/bin", "/sbin"],
		onlyif  => ["test -f /etc/init.d/$instance_name",
				"test -f $repo/REINSTALL",
				"test -f $repo/tomtopweb-1.0-SNAPSHOT.tgz"],
		logoutput => true,
	} ->
	exec { "rm -rf /usr/share/tomtopweb/$instance_name":
		path    => ["/usr/bin", "/bin", "/sbin"],
		onlyif  => ["test -f $repo/REINSTALL", "test -f $repo/tomtopweb-1.0-SNAPSHOT.tgz"],
		logoutput => true,
	} ->
	archive::extract {"$repo/tomtopweb-1.0-SNAPSHOT":
		target           => "/usr/share/tomtopweb/$instance_name",
		extension        => "tgz",
		root_dir         => ".",
		src_target       => "/",
		strip_components => 1,
		notify  => Exec["rolling_backup_$instance_name"],
	} ->
	exec { "rolling_backup_$instance_name":
		command => "mkdir -p \"/usr/share/tomtopweb/archive/$instance_name/`stat -c %y $repo/tomtopweb-1.0-SNAPSHOT.tgz`\"; \
				mv $repo/* \"/usr/share/tomtopweb/archive/$instance_name/`stat -c %y $repo/tomtopweb-1.0-SNAPSHOT.tgz`\"",
		path    => ["/usr/bin", "/bin", "/sbin"],
		onlyif  => ["test -f $repo/tomtopweb-1.0-SNAPSHOT.tgz"],
		logoutput => true,
	} ->
	file { "/etc/init.d/$instance_name":
		owner   => 'root',
		group   => 'root',
		mode    => '0755',
		content => template("tomtopweb/init.erb"),
		notify  => Service[$instance_name],
	} ->
	file { "/usr/share/tomtopweb/$instance_name/conf/application.conf":
		owner   => 'root',
		group   => 'root',
		mode    => '0644',
		content => template("tomtopweb/application.conf.erb"),
		notify  => Service[$instance_name],
	} ->
	file { "/usr/share/tomtopweb/$instance_name/conf/application-logger.xml":
		owner   => 'root',
		group   => 'root',
		mode    => '0644',
		content => template("tomtopweb/logger.xml.erb"),
		notify  => Service[$instance_name],
	} ->
	file { "/usr/share/tomtopweb/$instance_name/conf/robots.txt":
		owner   => 'root',
		group   => 'root',
		mode    => '0644',
		content => template("tomtopweb/robots.txt.erb"),
		notify  => Service[$instance_name],
	} ->
	service { "$instance_name":
		ensure     => $service_ensure,
		enable     => $service_enable,
		name       => $instance_name,
		hasstatus  => true,
		hasrestart => true,
		require => [ File["/etc/init.d/$instance_name"] ],
	}

	#
	# LIVE setup, need re-run puppet for new facts to be effective
	#
	exec { "live-setup-for-$port":
		path    => ["/usr/bin", "/bin", "/sbin"],
		onlyif  => "test -f $repo/LIVE",
		command => "echo $port > /usr/share/tomtopweb/LIVE",
	}
}
