/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.photos;

import com.cedarsoft.crypt.Hash;
import com.cedarsoft.photos.di.Modules;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.IOException;

import static com.cedarsoft.photos.ImageStorage.ALGORITHM;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DeleteRunner {
  public static void main(String[] args) throws IOException {
    System.out.println("Deleting some files");

    Injector injector = Guice.createInjector(Modules.getModules());
    ImageStorage imageStorage = injector.getInstance(ImageStorage.class);

    //imageStorage.delete(Hash.fromHex(ALGORITHM, "2af9cbae4148affc46bbf1c6596e3714e89d603d8c5ce6051c616e40d59a34d0"));
    //imageStorage.delete(Hash.fromHex(ALGORITHM, "dc3b4e8958646cbd8fe79e6ab6fa960b9803e2a85774107125bf33d1d28e0e8e"));
    //imageStorage.delete(Hash.fromHex(ALGORITHM, "242e87b94cfbd64ce6227bb4c478e4807b140dd9703325f727665c50d9333f00"));
    //imageStorage.delete(Hash.fromHex(ALGORITHM, "911e22bb1f5c832390654b7cbb5a7e6619138afbec65764e391682abea570097"));
    //imageStorage.delete(Hash.fromHex(ALGORITHM, "347383e1f2ca92e09b65722f09e150eaac853e720db56e89fd917441796bdfcd"));
    //imageStorage.delete(Hash.fromHex(ALGORITHM, "adb3188a7b03e42a889f1969b6ab46bcbea5195e9ae842d8e51baacac796fa7e"));
    //imageStorage.delete(Hash.fromHex(ALGORITHM, "3c31eb26edf94ad47f14641f5adfa4d8ca5be8ba6913f8b0046b4f1a0df8dba8"));
    //imageStorage.delete(Hash.fromHex(ALGORITHM, "8142dc1eb56f6dd77d9a93afba9a47b227b7799f7edf75e80e9b18cabb221ea8"));
    //imageStorage.delete(Hash.fromHex(ALGORITHM, "de017326eeb48554088904792569650ef08227e4cccdfd5bfd22bc6d854b12cb"));
  }
}
