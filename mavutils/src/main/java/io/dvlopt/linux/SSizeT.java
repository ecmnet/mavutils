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


package io.dvlopt.linux ;


import com.sun.jna.IntegerType ;
import com.sun.jna.Native      ;




/**
 * Class representing the native <code>ssize_t</code> type which is architecture-specific.
 * <p>
 * It is a signed integer typically representing the length of something (eg. the number of bytes that
 * have been read) where negative values represent an error (eg. -1 means nothing has been read because
 * something failed).
 */
public class SSizeT extends IntegerType {


    /**
     * In bytes, the size of <code>ssize_t</code>.
     */
    public static final int SIZE = Native.SIZE_T_SIZE ;




    /**
     * Constructor defaulting to 0.
     */
    public SSizeT() {
    
        this( 0 ) ;
    }




    /**
     * Constructor with a value.
     *
     * @param value  The original value, cannot be larger than the native <code>ssize_t</code>.
     */
    public SSizeT( long value ) {
    
        super( SIZE  ,
               value  ,
               false ) ;
    }
}
