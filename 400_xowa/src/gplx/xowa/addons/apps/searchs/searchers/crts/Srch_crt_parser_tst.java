/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012 gnosygnu@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package gplx.xowa.addons.apps.searchs.searchers.crts; import gplx.*; import gplx.xowa.*; import gplx.xowa.addons.*; import gplx.xowa.addons.apps.*; import gplx.xowa.addons.apps.searchs.*; import gplx.xowa.addons.apps.searchs.searchers.*;
import org.junit.*; import gplx.xowa.addons.apps.searchs.parsers.*; import gplx.xowa.addons.apps.searchs.searchers.crts.visitors.*;
public class Srch_crt_parser_tst {
	private final    Srch_crt_parser_fxt fxt = new Srch_crt_parser_fxt();
	@Test   public void Word__one()				{fxt.Test__parse("a"					, "a");}
	@Test   public void And__one()				{fxt.Test__parse("a + b"				, "(a AND b)");}
	@Test   public void And__many()				{fxt.Test__parse("a + b + c"			, "(a AND b AND c)");}
	@Test   public void And__dangling()			{fxt.Test__parse("a +"					, "a");}
	@Test   public void And__dupe()				{fxt.Test__parse("a + + b"				, "(a AND b)");}
	@Test   public void Quote()					{fxt.Test__parse("\"a b\""				, "\"a b\"");}
	@Test   public void Auto__word()			{fxt.Test__parse("a b"					, "(a AND b)");}
	@Test   public void Auto__quote()			{fxt.Test__parse("a \"b\""				, "(a AND \"b\")");}
	@Test   public void Auto__parens()			{fxt.Test__parse("a (b , c)"			, "(a AND (b OR c))");}
	@Test   public void Or__one()				{fxt.Test__parse("a , b"				, "(a OR b)");}
	@Test   public void Or__many()				{fxt.Test__parse("a , b , c"			, "(a OR b OR c)");}
	@Test   public void Mixed__3()				{fxt.Test__parse("a + b , c"			, "((a AND b) OR c)");}
	@Test   public void Mixed__5()				{fxt.Test__parse("a + b , c + d , e"	, "((((a AND b) OR c) AND d) OR e)");}
	@Test   public void Parens__basic() 		{fxt.Test__parse("a + (b , c)"			, "(a AND (b OR c))");}
	@Test   public void Parens__nest() 			{fxt.Test__parse("a + (b , (c + d))"	, "(a AND (b OR (c AND d)))");}
	@Test   public void Parens__mid() 			{fxt.Test__parse("a + (b , c) + d)"		, "(a AND (b OR c) AND d)");}
	@Test   public void Parens__mixed() 		{fxt.Test__parse("a + (b , c) , d)"		, "((a AND (b OR c)) OR d)");}
	@Test   public void Parens__dupe() 			{fxt.Test__parse("((a))"				, "a");}
	@Test   public void Parens__dangling__lhs() {fxt.Test__parse("(a"					, "a");}
	@Test   public void Parens__dangling__rhs() {fxt.Test__parse("a)"					, "a");}
	@Test   public void Parens__empty__bos()	{fxt.Test__parse("()"					, "");}
	@Test   public void Parens__empty__mid()	{fxt.Test__parse("a () b"				, "(a AND b)");}
	@Test   public void Not__bos() 				{fxt.Test__parse("-abc"					, "NOT abc");}
	@Test   public void Not__mid() 				{fxt.Test__parse("a -b"					, "(a AND NOT b)");}
	@Test   public void Not__2() 				{fxt.Test__parse("a -b -c"				, "(a AND NOT b AND NOT c)");}
	@Test   public void Not__dangling__eos() 	{fxt.Test__parse("a -"					, "a");}
	@Test   public void Not__dangling__mid() 	{fxt.Test__parse("a -- b"				, "(a AND b)");}	// NOTE: scanner will remove spaces and convert to "a", "--", "b"
	@Test   public void Not__dupe__2() 			{fxt.Test__parse("a --b"				, "(a AND b)");}
	@Test   public void Not__dupe__3() 			{fxt.Test__parse("a ---b"				, "(a AND NOT b)");}
	@Test   public void Not__parens() 			{fxt.Test__parse("a -(b + c)"			, "(a AND NOT (b AND c))");}
	@Test   public void Escape__eos()			{fxt.Test__parse("\\"					, "");}
	@Test   public void Escape__escaped()		{fxt.Test__parse("\\\\*"				, "\\*");}			// '\\' -> '\*'
}
class Srch_crt_parser_fxt {
	private final    Srch_crt_parser crt_parser;
	private final    Srch_crt_visitor__print visitor__to_str = new Srch_crt_visitor__print();
	public Srch_crt_parser_fxt() {
		crt_parser = new Srch_crt_parser(Srch_crt_scanner_syms.Dflt);
		Srch_text_parser text_parser = new Srch_text_parser();
		text_parser.Init_for_ttl(gplx.xowa.langs.cases.Xol_case_mgr_.A7());
	}
	public void Test__parse(String src_str, String expd) {
		byte[] src_bry = Bry_.new_a7(src_str);
		Srch_crt_mgr crt_mgr = crt_parser.Parse_or_invalid(src_bry, Bool_.N);
		Tfds.Eq(expd, String_.new_u8(visitor__to_str.Print(crt_mgr.Root)));
	}
}