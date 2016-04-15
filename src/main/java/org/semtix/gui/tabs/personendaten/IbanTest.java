/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.semtix.gui.tabs.personendaten;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public final class IbanTest {

    public static final int IBANNUMBER_MIN_SIZE = 15;
    public static final int IBANNUMBER_MAX_SIZE = 34;
    public static final BigInteger IBANNUMBER_MAGIC_NUMBER = new BigInteger("97");

	static final Map<String, Integer> isoPairs;

	//CountryCode+Lenght of IBAN: check http://www.swift.com/dsp/resources/documents/IBAN_Registry.pdf
	static final String iso = "AD24 AE23 AL28 AO25 AT20 AZ28 BA20 BG22 BE16 BF27 BH22 BI16 BJ28 BR29 "
			+ "CH21 CI28 CM27 CR21 CV25 CY28 CZ24 DE22 DK18 DO28 DZ24 EE20 ES24 FO18 FI18 FR27 GB22 GE22 GI23 "
			+ "GL18 GR27 GT28 HR21 HU28 IE22 IL23 IR26 IS26 IT27 JO30 KW30 KZ20 LB28 LC32 LI21 "
			+ "LT20 LU20 LV21 MC27 MD24 ME22 MG27 MK19 ML28 MR27 MT31 MU30 MZ25 NL18 NO15 PK24 PL28 "
			+ "PS29 PT25 QA29 RO24 RS22 SA24 SE24 SI19 SK24 SM27 SN28 TL23 TN24 TR26 UA29 VG24 XK20";

	static {
		isoPairs = new HashMap<>();
		for (String p : iso.split(" "))
			isoPairs.put(p.substring(0, 2), Integer.parseInt(p.substring(2)));
	}

	/**
	 * @param iban String without spaces and all uppercase
	 * @return true if iban country code is recognized and iban has correct length
	 */
	static boolean correctLen(String iban) {
		int len = iban.length();
		if (len < IBANNUMBER_MIN_SIZE || len > IBANNUMBER_MAX_SIZE)
			return false;
		try {
			if (isoPairs.get(iban.substring(0, 2)) != len)
				return false;
		} catch (NullPointerException np) {
			//unkown or wrong country code
			return false;
		}
		return true;
	}


	/**
	 * @param iban iban string (may contain lowercase letters and whitespaces)
	 * @return true if iban checksum is correct
	 */
	static boolean ibanTest(String iban) {
		iban = iban.replaceAll("\\s", "").toUpperCase();

		if (!correctLen(iban))
			return false;

		iban = iban.substring(4) + iban.substring(0, 4);

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < iban.length(); i++)
			sb.append(Character.digit(iban.charAt(i), 36));

		BigInteger bigInt = new BigInteger(sb.toString());

		return bigInt.mod(IBANNUMBER_MAGIC_NUMBER).intValue() == 1;
	}
}

