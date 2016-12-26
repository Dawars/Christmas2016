Christmas 2016
==============

A Christmas project inspired by http://ipv6tree.bitnet.be/

The goal was to get to know with Android Things and put together something simple but fun and interactive.
![Demo](images/xmas_led.jpg?raw=true "Demo")

The individual LEDs can be toggled remotely by sending UDP packets to dawars.no-ip.org:1234

Example: 'on 5' - turns the 6th LED on

Pre-requisites
--------------

- Android Things compatible board
- Android Studio 2.2+
- LEDs


Build and install
=================

On Android Studio, click on the "Run" button.

If you prefer to run on the command line, type

```bash
./gradlew installDebug
adb shell am start me.dawars.christmas2016/.MainActivity
```

License
-------

Copyright 2016 The Android Open Source Project, Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
