class fw::pre {
    Firewall {
      require => undef,
    }
    
    # Default firewall rules
    firewall { '000 accept all icmp':
      proto   => 'icmp',
      action  => 'accept',
    }->
    firewall { '001 accept all to lo interface':
      proto   => 'all',
      iniface => 'lo',
      action  => 'accept',
    }->
    firewall { "002 reject local traffic not on loopback interface":
      iniface     => '! lo',
      proto       => 'all',
      destination => '127.0.0.1/8',
      action      => 'reject',
    }->
    firewall { '003 accept related established rules':
      proto   => 'all',
      state => ['RELATED', 'ESTABLISHED'],
      action  => 'accept',
    }->
    firewall { '099 drop frequent ssh connections for 5 times per minute':
      proto   => 'tcp',
      port    => 22,
      action  => 'drop',
      recent  => 'update',
      rseconds  => '60',
      rhitcount => '5',
      rname     => 'SSH',
      rsource   => true,
    }->
    firewall { '100 allow ssh':
      chain   => 'INPUT',
      state   => ['NEW'],
      dport   => '22',
      proto   => 'tcp',
      action  => 'accept',
      recent  => 'set',
      rname   => 'SSH',
      rsource => true,
    }
}
