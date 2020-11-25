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


import com.sun.jna.Native;




/**
 * Exception associated with an errno value for when an error occurs on the native side.
 */
public class LinuxException extends Exception {


    private int errno ;




    /**
     * Constructor automatically fetching errno.
     */
    public LinuxException() {
    
        super( messagePrefix() ) ;

        this.errno = Native.getLastError() ;
    }




    /**
     * Constructor automatically fetching errno and associating it with a message.
     *
     * @param message  String describing what happened.
     */
    public LinuxException( String message ) {

        super( messagePrefix() + " : " + message ) ;

        this.errno = Native.getLastError() ;
    }




    // Makes a prefix stating the current value of errno for the exception message.
    //
    private static String messagePrefix() {

        return "Errno " + Native.getLastError() ;
    }




    /**
     * Retrieves the value of errno when the error occured.
     *
     * @return  The value of errno.
     */
    public int getErrno() {

        return this.errno ;
    }
}
