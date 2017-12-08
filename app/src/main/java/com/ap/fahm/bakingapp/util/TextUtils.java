/*
 * Copyright 2017.  FahmeemAPps
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ap.fahm.bakingapp.util;

public class TextUtils {

    public static String capitalizeEachWords(String text) {
        String[] stringArray = text.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : stringArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            stringBuilder.append(cap).append(" ");
        }
        return stringBuilder.toString();
    }

    public static String removeTrailingZero(String stringNumber) {
        return !stringNumber.contains(".") ? stringNumber :
                stringNumber.replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    public static String getExtension(String string) {
        if (string.length() == 3) {
            return string;
        } else if (string.length() > 3) {
            return string.substring(string.length() - 3);
        } else {
            throw new IllegalArgumentException("Word has less than 3 characters!");
        }
    }

}
