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
package gplx.xowa.xtns.pfuncs.wikis; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.pfuncs.*;
import org.junit.*; import gplx.xowa.wikis.nss.*; import gplx.xowa.wikis.data.*; import gplx.xowa.wikis.data.tbls.*;
public class Pfunc_pagesincategory_tst {
	private final Pfunc_pagesincategory_tstr tstr = new Pfunc_pagesincategory_tstr();
	@Before	public void setup()	{tstr.Init(); tstr.Init_category_counts("A", 1000, 2000, 3000);}
	@Test   public void Type__none()				{tstr.Test_parse("{{PAGESINCATEGORY:A}}"			, "6,000");}
	@Test   public void Type__none__fmt()			{tstr.Test_parse("{{PAGESINCATEGORY:A|R}}"			, "6000");}
	@Test   public void Type__page__1st()			{tstr.Test_parse("{{PAGESINCATEGORY:A|pages}}"		, "1,000");}
	@Test   public void Type__subc__1st()			{tstr.Test_parse("{{PAGESINCATEGORY:A|subcats}}"	, "2,000");}
	@Test   public void Type__file__1st()			{tstr.Test_parse("{{PAGESINCATEGORY:A|files}}"		, "3,000");}
	@Test   public void Type__page__2nd()			{tstr.Test_parse("{{PAGESINCATEGORY:A|R|pages}}"	, "1000");}
	@Test   public void Type__subc__2nd()			{tstr.Test_parse("{{PAGESINCATEGORY:A|R|subcats}}"	, "2000");}
	@Test   public void Type__file__2nd()			{tstr.Test_parse("{{PAGESINCATEGORY:A|R|files}}"	, "3000");}
	@Test   public void Zero__no_title()			{tstr.Test_parse("{{PAGESINCATEGORY:}}"				, "0");}
	@Test   public void Zero__missing_title()		{tstr.Test_parse("{{PAGESINCATEGORY:Missing}}"		, "0");}
	@Test   public void Wrong_args()				{tstr.Test_parse("{{PAGESINCATEGORY:A|invalid|x}}"	, "6,000");}	// defaults to all,fmt
}
class Pfunc_pagesincategory_tstr {
	private final Xop_fxt parser_tstr;
	private final Xoae_app app; private final Xowe_wiki wiki;
	private final Xowd_db_mgr core_data_mgr;
	private final Xowd_page_tbl page_tbl; private final Xowd_cat_core_tbl cat_core_tbl;
	public Pfunc_pagesincategory_tstr() {
		Xoa_test_.Inet__init();
		this.app = Xoa_app_fxt.app_();
		this.wiki = Xoa_app_fxt.wiki_tst_(app);
		Xoa_test_.Db__init__mem(wiki);
		this.parser_tstr = new Xop_fxt(app, wiki);
		this.core_data_mgr = wiki.Data__core_mgr();
		this.page_tbl = core_data_mgr.Tbl__page();
		this.cat_core_tbl = core_data_mgr.Db__cat_core().Tbl__cat_core();
	}
	public void Init() {
		parser_tstr.Reset();
	}
	public void Init_category_counts(String category_title, int pages, int subcs, int files) {
		int page_id = 1;
		page_tbl.Insert_bgn();
		page_tbl.Insert_cmd_by_batch(page_id, Xow_ns_.Id_category, Bry_.new_u8(category_title), Bool_.N, DateAdp_.Now(), 1, 1, 1, 1);
		page_tbl.Insert_end();
		cat_core_tbl.Insert_bgn();
		cat_core_tbl.Insert_cmd_by_batch(page_id, pages, subcs, files, Byte_.Zero, 1);
		cat_core_tbl.Insert_end();
	}
	public void Test_parse(String raw, String expd) {
		parser_tstr.Test_html_full_str(raw, expd);
	}
}