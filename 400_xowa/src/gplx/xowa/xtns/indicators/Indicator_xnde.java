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
package gplx.xowa.xtns.indicators; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.core.primitives.*; import gplx.xowa.htmls.core.htmls.*; import gplx.xowa.wikis.pages.skins.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.xndes.*; import gplx.xowa.parsers.htmls.*;
public class Indicator_xnde implements Xox_xnde, Mwh_atr_itm_owner {
	public String Name() {return name;} private String name;
	public byte[] Html() {return html;} private byte[] html;
	public void Init_for_test(String name, byte[] html) {this.name = name; this.html = html;}	// TEST
	public void Xatr__set(Xowe_wiki wiki, byte[] src, Mwh_atr_itm xatr, Object xatr_id_obj) {
		if (xatr_id_obj == null) return;
		Byte_obj_val xatr_id = (Byte_obj_val)xatr_id_obj;
		switch (xatr_id.Val()) {
			case Xatr_name:		this.name = xatr.Val_as_str(); break;
		}
	}
	public void Xtn_parse(Xowe_wiki wiki, Xop_ctx ctx, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		Xox_xnde_.Xatr__set(wiki, this, xatrs_hash, src, xnde);
		this.html = Xop_parser_.Parse_text_to_html(wiki, ctx.Cur_page(), ctx.Cur_page().Ttl(), Bry_.Mid(src, xnde.Tag_open_end(), xnde.Tag_close_bgn()), false);
		Indicator_html_bldr html_bldr = ctx.Cur_page().Html_data().Indicators();
		if (this.name != null) html_bldr.Add(this);	// NOTE: must do null-check b/c Add will use Name as key for hashtable
	}
	public void Xtn_write(Bry_bfr bfr, Xoae_app app, Xop_ctx ctx, Xoh_html_wtr html_wtr, Xoh_wtr_ctx hctx, Xop_xnde_tkn xnde, byte[] src) {
		if (this.name == null) bfr.Add_str_a7("Error: Page status indicators' name attribute must not be empty.");	
	}
	private static final byte Xatr_name = 0;
	private static final Hash_adp_bry xatrs_hash = Hash_adp_bry.ci_a7()
	.Add_str_obj("name", Byte_obj_val.new_(Xatr_name));
}