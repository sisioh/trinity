/*
 * Copyright 2013 Sisioh Project and others. (http://sisioh.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.sisioh.trinity.domain.mvc.application

import org.sisioh.trinity.Environment

/**
 * Represents the trait to support the console application.
 */
trait ConsoleApplicationSupport extends Bootstrap {
  this: ConsoleApplication =>

  override protected lazy val environment: Environment.Value =
    if (args.size > 0 && args(0).toLowerCase == Environment.Development.toString.toLowerCase)
      Environment.Development
    else
      Environment.Product

}
