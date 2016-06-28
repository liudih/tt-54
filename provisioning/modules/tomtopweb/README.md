# TOMTOP Website Deployment

#### Table of Contents

1. [Overview](#overview)
2. [Module Description - What the module does and why it is useful](#module-description)
3. [Setup - The basics of getting started with tomtopweb](#setup)
    * [What tomtopweb affects](#what-tomtopweb-affects)
    * [Setup requirements](#setup-requirements)
    * [Beginning with tomtopweb](#beginning-with-tomtopweb)
4. [Usage - Configuration options and additional functionality](#usage)
5. [Reference - An under-the-hood peek at what the module is doing and how](#reference)
5. [Limitations - OS compatibility, etc.](#limitations)
6. [Development - Guide for contributing to the module](#development)

## Overview

We use this to deploy an instance of TOMTOP Website.

## Module Description

Website runs as its own process, listening to a specific port.
This module aims to provide multiple-instance deployment by specifying
different TGZ repository, port and instance name (service name).

Default installation: /usr/share/tomtopweb/<instance_name>
where instance_name has default "tomtopweb".

## Setup

### What tomtopweb affects

* /etc/init.d/<instance_name>
* /usr/share/tomtopweb/<instance_name>
* /var/log/<instance_name>

### Setup Requirements **OPTIONAL**

Java8 is required.

### Beginning with TOMTOP Website

The following setup a service instance with port listening 9000 with default (UAT) settings.

```
        tomtopweb::instance { "tomtopweb-9000":
                instance_name => "tomtopweb-9000",
                repo => "/deploy-9000",
                port => 9000,
        }
```

## Usage

Put the classes, types, and resources for customizing, configuring, and doing
the fancy stuff with your module here.

## Reference

Module:
* camptocamp/archive (used to untar the distribution)

## Limitations

TODO

## Development

TODO

