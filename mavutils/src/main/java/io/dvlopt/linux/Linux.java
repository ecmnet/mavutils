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


import com.sun.jna.Native ;




/**
 * Class containing static methods related to Linux.
 */
public class Linux {


    // Privatize constructor.
    //
    private Linux() {}




    /**
     * Get the value of <code>errno</code>, a global int often set by native functions when
     * a failure occurs.
     *
     * @return  The current value of errno.
     */
    public static int getErrno() {
    
        return Native.getLastError() ;
    }




    /**
     * Set the value of <code>errno</code>.
     *
     * @param value The new value of errno.
     *
     * @see getErrno
     */
    public static void setErrno( int value ) {

        Native.setLastError( value ) ;
    }
}
