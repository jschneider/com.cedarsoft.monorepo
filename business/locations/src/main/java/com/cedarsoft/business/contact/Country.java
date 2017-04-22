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

package com.cedarsoft.business.contact;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import javax.annotation.Nullable;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Represents a country
 */
public enum Country {
  Afghanistan( "AF" ),
  Albania( "AL" ),
  Algeria( "DZ" ),
  AmericanSamoa( "AS" ),
  Andorra( "AD" ),
  Angola( "AO" ),
  Anguilla( "AI" ),
  Antarctica( "AQ" ),
  AntiguaandBarbuda( "AG" ),
  Argentina( "AR" ),
  Armenia( "AM" ),
  Aruba( "AW" ),
  Australia( "AU" ),
  Austria( "AT" ),
  Azerbaijan( "AZ" ),
  Bahamas( "BS" ),
  Bahrain( "BH" ),
  Bangladesh( "BD" ),
  Barbados( "BB" ),
  Belarus( "BY" ),
  Belgium( "BE" ),
  Belize( "BZ" ),
  Benin( "BJ" ),
  Bermuda( "BM" ),
  Bhutan( "BT" ),
  Bolivia( "BO" ),
  BosniaandHerzegowina( "BA" ),
  Botswana( "BW" ),
  BouvetIsland( "BV" ),
  Brazil( "BR" ),
  BritishIndianOceanTerritory( "IO" ),
  BruneiDarussalam( "BN" ),
  Bulgaria( "BG" ),
  BurkinaFaso( "BF" ),
  Burundi( "BI" ),
  Cambodia( "KH" ),
  Cameroon( "CM" ),
  Canada( "CA" ),
  CapeVerde( "CV" ),
  CaymanIslands( "KY" ),
  CentralAfricanRepublic( "CF" ),
  Chad( "TD" ),
  Chile( "CL" ),
  China( "CN" ),
  ChristmasIsland( "CX" ),
  CocosIslands( "CC" ),
  Columbia( "CO" ),
  Comoros( "KM" ),
  Congo( "CG" ),
  CookIslands( "CK" ),
  CostaRica( "CR" ),
  CoteDIvoire( "CI" ),
  Croatia( "HR" ),
  Cuba( "CU" ),
  Cyprus( "CY" ),
  CzechRepublic( "CZ" ),
  Denmark( "DK" ),
  Djibouti( "DJ" ),
  Dominica( "DM" ),
  DominicanRepublic( "DO" ),
  EastTimor( "TP" ),
  Ecuador( "EC" ),
  Egypt( "EG" ),
  ElSalvador( "SV" ),
  EquatorialGuinea( "GQ" ),
  Eritrea( "ER" ),
  Estonia( "EE" ),
  Ethiopia( "ET" ),
  FalklandIslands( "FK" ),
  FaroeIslands( "FO" ),
  Fiji( "FJ" ),
  Finland( "FI" ),
  France( "FR" ),
  FranceMetropolitan( "FX" ),
  FrenchGuiana( "GF" ),
  FrenchPolynesia( "PF" ),
  FrenchSouthernTerritories( "TF" ),
  Gabon( "GA" ),
  Gambia( "GM" ),
  Georgia( "GE" ),
  Germany( "DE" ),
  Ghana( "GH" ),
  Gibraltar( "GI" ),
  Greece( "GR" ),
  Greenland( "GL" ),
  Grenada( "GD" ),
  Guadeloupe( "GP" ),
  Guam( "GU" ),
  Guatemala( "GT" ),
  Guinea( "GN" ),
  GuineaBissau( "GW" ),
  Guyana( "GY" ),
  Haiti( "HT" ),
  HeardandMcDonaldIslands( "HM" ),
  Honduras( "HN" ),
  HongKong( "HK" ),
  Hungary( "HU" ),
  Iceland( "IS" ),
  India( "IN" ),
  Indonesia( "ID" ),
  Iran( "IR" ),
  Iraq( "IQ" ),
  Ireland( "IE" ),
  Israel( "IL" ),
  Italy( "IT" ),
  Jamacia( "JM" ),
  Japan( "JP" ),
  Jordan( "JO" ),
  Kazakhstan( "KZ" ),
  Kenya( "KE" ),
  Kiribati( "KI" ),
  Korea_DemocraticPeoplesRepublicof( "KP" ),
  Korea_Republicof( "KR" ),
  Kuwait( "KW" ),
  Kyrgyzstan( "KG" ),
  LaoPeoplesDemocraticRepublic( "LA" ),
  Latvia( "LV" ),
  Lebanon( "LB" ),
  Lesotho( "LS" ),
  Liberia( "LR" ),
  Libyan_ArabJamahiriya( "LY" ),
  Liechtenstein( "LI" ),
  Lithuania( "LT" ),
  Luxembourg( "LU" ),
  Macau( "MO" ),
  Macedonia( "MK" ),
  Madagascar( "MG" ),
  Malawi( "MW" ),
  Malaysia( "MY" ),
  Maldives( "MV" ),
  Mali( "ML" ),
  Malta( "MT" ),
  MarshallIslands( "MH" ),
  Martinique( "MQ" ),
  Mauritania( "MR" ),
  Mauritius( "MU" ),
  Mayotte( "YT" ),
  Mexico( "MX" ),
  Micronesia_FederatedStatesof( "FM" ),
  Moldova_Republicof( "MD" ),
  Monaco( "MC" ),
  Mongolia( "MN" ),
  Montserrat( "MS" ),
  Morocco( "MA" ),
  Mozambique( "MZ" ),
  Burma( "MM" ),
  Namibia( "NA" ),
  Nauru( "NR" ),
  Nepal( "NP" ),
  Netherlands( "NL" ),
  NetherlandsAntilles( "AN" ),
  NewCaledonia( "NC" ),
  NewZealand( "NZ" ),
  Nicaragua( "NI" ),
  Niger( "NE" ),
  Nigeria( "NG" ),
  Niue( "NU" ),
  NorfolkIsland( "NF" ),
  NorthernMarianaIslands( "MP" ),
  Norway( "NO" ),
  Oman( "OM" ),
  Pakistan( "PK" ),
  Palau( "PW" ),
  Panama( "PA" ),
  PapuaNewGuinea( "PG" ),
  Paraguay( "PY" ),
  Peru( "PE" ),
  Philippines( "PH" ),
  Pitcairn( "PN" ),
  Poland( "PL" ),
  Portugal( "PT" ),
  Qatar( "QA" ),
  Reunion( "RE" ),
  Romania( "RO" ),
  RussianFederation( "RU" ),
  Rwanda( "RW" ),
  SaintKettsAndNevis( "KN" ),
  SaintLucia( "LC" ),
  SaintVincentandtheGrenadines( "VC" ),
  Samoa( "WS" ),
  SanMarino( "SM" ),
  SaoTomeandPrincipe( "ST" ),
  SaudiArabia( "SA" ),
  Senegal( "SN" ),
  Seychelles( "SC" ),
  SierraLeone( "SL" ),
  Singapore( "SG" ),
  Solvakia( "SK" ),
  Slovenia( "SI" ),
  SolomanIslands( "SO" ),
  Somalia( "SO" ),
  SouthAfrica( "ZA" ),
  SouthGeorgiaandtheSouthSandwichIslands( "GS" ),
  Spain( "ES" ),
  SriLanka( "LK" ),
  StHelena( "SH" ),
  StPierraandMiquelon( "PM" ),
  Sudan( "SD" ),
  Suriname( "SR" ),
  SvalbardandJanMayenIslands( "SJ" ),
  Swaziland( "SZ" ),
  Sweden( "SE" ),
  Switzerland( "CH" ),
  SyrianArabRepublic( "SY" ),
  Taiwan_Republicof( "TW" ),
  Tajikistan( "TJ" ),
  Tanzania_UnitedRepublicof( "TZ" ),
  Thailand( "TH" ),
  Togo( "TG" ),
  Tokelau( "TK" ),
  Tonga( "TO" ),
  TrinidadandTobago( "TT" ),
  Tunisia( "TN" ),
  Turkey( "TR" ),
  Turmenistan( "TM" ),
  TurksandCaicosIslands( "TC" ),
  Tuvalu( "TV" ),
  Uganda( "UG" ),
  Ukraine( "UA" ),
  UnitedArabEmirates( "AE" ),
  UnitedKingdom( "GB" ),
  UnitedStates( "US" ),
  UnitedStatesMinorOutlyingIslands( "UM" ),
  Uruguay( "UY" ),
  Uzbekistan( "UZ" ),
  Vanuatu( "VU" ),
  VaticanCityState( "VA" ),
  Venezuela( "VE" ),
  Vietnam( "VN" ),
  VirginIslands_British( "VG" ),
  VirginIslands_US( "VI" ),
  WallisandFutunaIslands( "WF" ),
  WesternSahara( "EH" ),
  Yemen( "YE" ),
  Yugoslavia( "YU" ),
  Zaire( "ZR" ),
  Zambia( "ZM" ),
  Zimbabwe( "ZW" );


  private final String isoCode;
  @Nonnull

  static final String COUNTRY_NAMES_BUNDLE_NAME = "com.cedarsoft.business.contact.countryNames";

  /**
   * Creates a new country
   *
   * @param isoCode the iso code
   */
  Country(  @Nonnull String isoCode ) {
    this.isoCode = isoCode;
  }

  /**
   * Returns the name of the country
   *
   * @return the name of the country
   */
  @Nonnull

  public String getName() {
    return getName( this, null );
  }

  /**
   * Returns the translated name of the country
   *
   * @param locale the locale the name is translated to
   * @return the name of the country
   */
  @Nonnull

  public String getName( @Nonnull Locale locale ) {
    return getName( this, locale );
  }

  /**
   * Returns the iso code of the country
   *
   * @return the iso code
   */
  @Nonnull

  public String getIsoCode() {
    return isoCode;
  }

  /**
   * Returns the locale for the country
   *
   * @return the locale
   */
  @Nonnull
  public Locale getLocale() {
    for ( Locale locale : Locale.getAvailableLocales() ) {
      if ( locale.getCountry().equals( isoCode ) ) {
        return locale;
      }
    }
    throw new IllegalStateException( "Could not find a locale for ISO code " + isoCode );
  }


  @Override
  public String toString() {
    return getName();
  }

  /**
   * Returns the translated name of the country
   *
   * @param country the country
   * @param locale  the locale
   * @return the name of the country
   */
  @Nonnull
  public static String getName( @Nonnull Country country, @Nullable Locale locale ) {
    try {
      ResourceBundle bundle;
      if ( locale == null ) {
        bundle = ResourceBundle.getBundle( COUNTRY_NAMES_BUNDLE_NAME );
      } else {
        bundle = ResourceBundle.getBundle( COUNTRY_NAMES_BUNDLE_NAME, locale );
      }
      return bundle.getString( country.getIsoCode() );
    } catch ( MissingResourceException ignore ) {
      return "<unknown " + country.getIsoCode() + '>';
    }
  }

  /**
   * Returns the default country
   *
   * @return the default country
   */
  @Nonnull
  public static Country getDefaultCountry() {
    return findCountry( Locale.getDefault().getCountry() );
  }

  @Nonnull
  public static Country findCountry(  @Nonnull String countryCode ) {
    for ( Country country : values() ) {
      if ( country.getIsoCode().equalsIgnoreCase( countryCode ) ) {
        return country;
      }
    }
    throw new IllegalArgumentException( "No country found with iso code " + countryCode );
  }

  @Nonnull
  public static Country findCountryWithName( @Nonnull String name ) {
    for ( Country country : values() ) {
      if ( country.getName().equalsIgnoreCase( name ) ) {
        return country;
      }
    }
    throw new IllegalArgumentException( "No country found with name " + name );
  }
}
