CentOS Puppet Install
=====================

We need to install puppet agent only.

    https://docs.puppetlabs.com/guides/install_puppet/install_el.html

How to RUN
==========

Create a Host manifest file like this:

    node 'app01' {
        class { 'tomtopweb':
                dbhost => 'xxxxx',
                dbuser => 'tomtop',
                dbpass => 'tomtop',
                session_host => 'xxxxx:6379',
                session_db => 0,
                es_host => 'xxxxx:9300',
        }
    }

Execute puppet apply locally:

    puppet apply --modulepath=modules your_host_manifest_file.pp


