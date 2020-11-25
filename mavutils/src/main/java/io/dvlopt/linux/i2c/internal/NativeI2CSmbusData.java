/*
 * Copyright 2018 Adam Helinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.dvlopt.linux.i2c.internal ;


import com.sun.jna.Union ;




/**
 * Internal class kept public for JNA to work, the user should not bother about this.
 */
public class NativeI2CSmbusData extends Union {


    // I2C_SMBUS_BLOCK_MAX( 32 ) + 2( 1 for length + 1 for PEC)
    //
    public static final int SIZE = 34 ;




    public byte   byt                      ;
    public short  word                     ;
    public byte[] block = new byte[ SIZE ] ;
}
