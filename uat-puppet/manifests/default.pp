define uat_instance ($instance_name, $repo, $port) {
	tomtopweb::instance { "$instance_name":
		instance_name => "$instance_name",
		repo => "$repo",
		port => $port,
		mem => '256',
		dbhost => 'tomtopwebsite-postgresql',
		dbuser => 'tomtopwebsite',
		dbpass => 'tomtopwebsite123',
		session_host => 'tomtopwebsite-redis:6379',
		session_db => 0,
		db_reset_allowed => false,
		es_host => 'tomtopwebsite-elasticsearch:9300',
		google_clientid => '1031779384204-umd79kdejl4q5sdul1hjs32c93437jop.apps.googleusercontent.com',
		google_clientsecret => '154BNkfOO9pxU0vZvC51y7e4',
		facebook_appid => '284737934897635',
		facebook_appsecret => '258c8396a87a4a58098cbbfd7f4b7b4f',
		paypal_sandbox => true,
		paypal_clientid => "ARDKFlmvmotUn45GaKd6o0eTFOPu245tnJ2aK9AFAr9H0Gd95cwp-GBcHkfc6VbOPN38Iw07iP5yv0El",
		paypal_secret => "EF50eGNy5TnyA_mb50Xd7HYSyTitrLdbchaanSymxB3phuk3FhYA9v0wwV6KoM2tQVJB9zO1rttQwsdG",
		paypal_return => "http://uat.tomtop.com/",
		paypal_notifyurl => "http://uat.tomtop.com/paypal/payment",
		oceanpayment_sandbox => true,
		yahoo_appkey => '13263',
		yahoo_sign => '6696acc5e1df54d584365ca4844bf76d',
		rspread_url => 'http://service.reasonablespread.com/',
		rspread_account => 'sales160@tomtop.com',
		rspread_password => 'Tt1428.EDM',
		order_step => 'old',
		robots_user_agent => '* Disallow: /',
	}
}
node default {
	uat_instance { 'tomtopweb-9000':
		instance_name => 'tomtopweb-9000',
		repo => '/deploy-9000',
		port => 9000,
	}
	uat_instance { 'tomtopweb-9001':
		instance_name => 'tomtopweb-9001',
		repo => '/deploy-9001',
		port => 9001,
	}
	class { 'nginx':
	}
	
	if ($tomtopweb_live == 9000) {
		$tomtopweb_staging = 9001
	} else {
		$tomtopweb_staging = 9000
	}

	nginx::resource::upstream { 'live':
		members => [ "localhost:$tomtopweb_live", ],
	}
	nginx::resource::upstream { 'staging':
		members => [ "localhost:$tomtopweb_staging", ],
	}

	file { '/etc/nginx/blockips.conf':
		ensure => present,
		content => template('tomtopweb/blockips.conf'),
		notify => Service['nginx']
	}

	nginx::resource::vhost { 'uat.tomtop.com':
		proxy => 'http://live',
		location_cfg_append => { 'rewrite' => ['^/media/catalog/(.*)$ http://img.tomtop.com/media/catalog/$1 permanent',
							'^/dropshipapi/(.*)$ http://img.tomtop.com/dropshipapi/$1 permanent',
							'^/blog$ http://blog.tomtop.com permanent'] },
		include_files => [ '/etc/nginx/blockips.conf' ],
		proxy_read_timeout => '600s',
	}
	nginx::resource::vhost { 'uats.tomtop.com':
		proxy => 'http://staging',
		location_cfg_append => { 'rewrite' => ['^/media/catalog/(.*)$ http://img.tomtop.com/media/catalog/$1 permanent',
							'^/dropshipapi/(.*)$ http://img.tomtop.com/dropshipapi/$1 permanent',
							'^/blog$ http://blog.tomtop.com permanent'] },
		include_files => [ '/etc/nginx/blockips.conf' ],
		proxy_read_timeout => '600s',
	}

	nginx::resource::location { 'dodocool.uat.tomtop.com':
		ensure   => present,
		www_root => '/var/www',
		location => '/dodocool',
		vhost    => 'uat.tomtop.com',
	}
	nginx::resource::location { 'app.uat.tomtop.com':
		ensure   => present,
		www_root => '/var/www',
		location => '/app',
		vhost    => 'uat.tomtop.com',
	}

	# firewall
	class { 'fw': }

	firewallchain { 'HTTPLIMIT:filter:IPv4':
		ensure  => present,
	}
	firewall {'100 limit http traffic':
		chain   => 'INPUT',
		state   => ['NEW'],
		dport   => '80',
		proto   => 'tcp',
		recent  => 'update',
		rseconds => '10',
		rhitcount => '10',
		jump    => 'HTTPLIMIT',
	}
	firewall {'101 log http/https traffic':
		chain   => 'HTTPLIMIT',
		proto   => 'tcp',
		limit   => '5/min',
		jump    => 'LOG',
		log_prefix => 'Rejected TCP: ',
		log_level => '7',
	}
	firewall {'102 reject http traffic':
		chain   => 'HTTPLIMIT',
		action  => 'reject',
	}
	firewall {'103 allow http':
		chain   => 'INPUT',
		state   => ['NEW'],
		dport   => '80',
		proto   => 'tcp',
		recent  => 'set',
		action  => 'accept',
	}
	firewall {'100 limit https traffic':
		chain   => 'INPUT',
		state   => ['NEW'],
		dport   => '443',
		proto   => 'tcp',
		recent  => 'update',
		rseconds => '1',
		rhitcount => '10',
		jump    => 'HTTPLIMIT',
	}
	firewall {'103 allow https':
		chain   => 'INPUT',
		state   => ['NEW'],
		dport   => '443',
		proto   => 'tcp',
		recent  => 'set',
		action  => 'accept',
	}

	package { 'crontabs':
		ensure  => installed,
	}

	service { 'crond':
		ensure => running,
		enable => true,
	}

	cron { update-ddc:
		command => "cd /var/www/dodocool; /usr/bin/git pull",
		user    => root,
		minute  => '*/5',
	}
	cron { update-app:
		command => "cd /var/www/app; /usr/bin/git pull",
		user    => root,
		minute  => '*/5',
	}
}
