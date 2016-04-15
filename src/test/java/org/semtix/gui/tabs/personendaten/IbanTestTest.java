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

import junit.framework.TestCase;

/**
 * Tests the IBAN-Tester
 * Created by Michael on 03.07.15.
 */
public class IbanTestTest extends TestCase {


    public void testIbanTest() throws Exception {

		assertTrue(IbanTest.ibanTest("GB82WEST12345698765432"));
		assertFalse(IbanTest.ibanTest("GB82TEST12345698765432 "));
		assertFalse(IbanTest.ibanTest("GB81WEST12345698765432"));
		assertTrue(IbanTest.ibanTest("SA0380000000608010167519"));
		assertTrue(IbanTest.ibanTest("CH9300762011623852957 "));


		assertTrue(IbanTest.ibanTest("DE02200411330580215200"));
		assertTrue(IbanTest.ibanTest("DE61200411330580215205"));
        assertTrue(IbanTest.ibanTest("DE73200411330580215280"));
        assertTrue(IbanTest.ibanTest("PL58124018641978001052645142"));
        assertTrue(IbanTest.ibanTest("GB67MIDL40413051651013"));
        assertTrue(IbanTest.ibanTest("NL46RABO0152346457"));
		assertFalse(IbanTest.ibanTest("IT71Q0200801404000091232916"));
		assertFalse(IbanTest.ibanTest("IT48V0306903201100000064505"));
		assertFalse(IbanTest.ibanTest("L47212110090000000235698741"));
		assertTrue(IbanTest.ibanTest("DZ4000400174401001050486"));
        assertTrue(IbanTest.ibanTest("AD1200012030200359100100"));
        assertTrue(IbanTest.ibanTest("AO06000600000100037131174"));
        assertTrue(IbanTest.ibanTest("AT611904300234573201"));
        assertTrue(IbanTest.ibanTest("AZ21NABZ00000000137010001944"));
        assertTrue(IbanTest.ibanTest("BH29BMAG1299123456BH00"));
        assertTrue(IbanTest.ibanTest("BA391290079401028494"));
        assertTrue(IbanTest.ibanTest("BE68539007547034"));
        assertTrue(IbanTest.ibanTest("BJ11B00610100400271101192591"));
        assertTrue(IbanTest.ibanTest("BR9700360305000010009795493P1"));
        assertTrue(IbanTest.ibanTest("BG80BNBG96611020345678"));
        assertTrue(IbanTest.ibanTest("BF1030134020015400945000643"));
        assertTrue(IbanTest.ibanTest("BI43201011067444"));
        assertTrue(IbanTest.ibanTest("CM2110003001000500000605306"));
        assertTrue(IbanTest.ibanTest("CV64000300004547069110176"));
        assertTrue(IbanTest.ibanTest("CR0515202001026284066"));
        assertTrue(IbanTest.ibanTest("HR1210010051863000160"));
        assertTrue(IbanTest.ibanTest("CY17002001280000001200527600"));
        assertTrue(IbanTest.ibanTest("CZ6508000000192000145399"));
        assertTrue(IbanTest.ibanTest("DK5000400440116243"));
        assertTrue(IbanTest.ibanTest("DO28BAGR00000001212453611324"));
        assertTrue(IbanTest.ibanTest("TL380080012345678910157"));
        assertTrue(IbanTest.ibanTest("EE382200221020145685"));
        assertTrue(IbanTest.ibanTest("FO1464600009692713"));
        assertTrue(IbanTest.ibanTest("FI2112345600000785"));
        assertTrue(IbanTest.ibanTest("FR1420041010050500013M02606"));
        assertTrue(IbanTest.ibanTest("GT82TRAJ01020000001210029690"));
        assertTrue(IbanTest.ibanTest("GE29NB0000000101904917"));
        assertTrue(IbanTest.ibanTest("DE89370400440532013000"));
        assertTrue(IbanTest.ibanTest("GI75NWBK000000007099453"));
        assertTrue(IbanTest.ibanTest("GR1601101250000000012300695"));
        assertTrue(IbanTest.ibanTest("GL8964710001000206"));
        assertTrue(IbanTest.ibanTest("HU42117730161111101800000000"));
        assertTrue(IbanTest.ibanTest("IS140159260076545510730339"));
        assertTrue(IbanTest.ibanTest("IR580540105180021273113007"));
        assertTrue(IbanTest.ibanTest("IE29AIBK93115212345678"));
        assertTrue(IbanTest.ibanTest("IL620108000000099999999"));
        assertTrue(IbanTest.ibanTest("IT60X0542811101000000123456"));
        assertTrue(IbanTest.ibanTest("CI05A00060174100178530011852"));
        assertTrue(IbanTest.ibanTest("JO94CBJO0010000000000131000302"));
        assertTrue(IbanTest.ibanTest("KZ176010251000042993"));
        assertTrue(IbanTest.ibanTest("KW74NBOK0000000000001000372151"));
        assertTrue(IbanTest.ibanTest("LV80BANK0000435195001"));
        assertTrue(IbanTest.ibanTest("LB30099900000001001925579115"));
        assertTrue(IbanTest.ibanTest("LI21088100002324013AA"));
        assertTrue(IbanTest.ibanTest("LT121000011101001000"));
        assertTrue(IbanTest.ibanTest("LU280019400644750000"));
        assertTrue(IbanTest.ibanTest("MK07300000000042425"));
        assertTrue(IbanTest.ibanTest("MG4600005030010101914016056"));
        assertTrue(IbanTest.ibanTest("MT84MALT011000012345MTLCAST001S"));
        assertTrue(IbanTest.ibanTest("MR1300012000010000002037372"));
        assertTrue(IbanTest.ibanTest("MU17BOMM0101101030300200000MUR"));
        assertTrue(IbanTest.ibanTest("ML03D00890170001002120000447"));
        assertTrue(IbanTest.ibanTest("MD24AG000225100013104168"));
        assertTrue(IbanTest.ibanTest("MC5813488000010051108001292"));
        assertTrue(IbanTest.ibanTest("ME25505000012345678951"));
        assertTrue(IbanTest.ibanTest("MZ59000100000011834194157"));
        assertTrue(IbanTest.ibanTest("NL91ABNA0417164300"));
        assertTrue(IbanTest.ibanTest("NO9386011117947"));
        assertTrue(IbanTest.ibanTest("PK24SCBL0000001171495101"));
        assertTrue(IbanTest.ibanTest("PS92PALS000000000400123456702"));
        assertTrue(IbanTest.ibanTest("PL27114020040000300201355387"));
        assertTrue(IbanTest.ibanTest("PT50000201231234567890154"));
        assertTrue(IbanTest.ibanTest("QA58DOHB00001234567890ABCDEFG"));
        assertTrue(IbanTest.ibanTest("XK051212012345678906"));
        assertTrue(IbanTest.ibanTest("RO49AAAA1B31007593840000"));
        assertTrue(IbanTest.ibanTest("SM86U0322509800000000270100"));
        assertTrue(IbanTest.ibanTest("SA0380000000608010167519"));
        assertTrue(IbanTest.ibanTest("SN12K00100152000025690007542"));
        assertTrue(IbanTest.ibanTest("RS35260005601001611379"));
        assertTrue(IbanTest.ibanTest("SK3112000000198742637541"));
        assertTrue(IbanTest.ibanTest("SI56191000000123438"));
        assertTrue(IbanTest.ibanTest("ES9121000418450200051332"));
        assertTrue(IbanTest.ibanTest("SE3550000000054910000003"));
        assertTrue(IbanTest.ibanTest("CH9300762011623852957"));
        assertTrue(IbanTest.ibanTest("TN5914207207100707129648"));
        assertTrue(IbanTest.ibanTest("TR330006100519786457841326"));
        assertTrue(IbanTest.ibanTest("AE260211000000230064016"));
        assertTrue(IbanTest.ibanTest("GB29NWBK60161331926819"));
        assertTrue(IbanTest.ibanTest("VG96VPVG0000012345678901"));

    }

}