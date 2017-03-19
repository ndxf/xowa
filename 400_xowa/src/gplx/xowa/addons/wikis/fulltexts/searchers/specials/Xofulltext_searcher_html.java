/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2017 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.addons.wikis.fulltexts.searchers.specials; import gplx.*; import gplx.xowa.*; import gplx.xowa.addons.*; import gplx.xowa.addons.wikis.*; import gplx.xowa.addons.wikis.fulltexts.*; import gplx.xowa.addons.wikis.fulltexts.searchers.*;
import gplx.xowa.specials.*; import gplx.langs.mustaches.*; import gplx.xowa.wikis.pages.*; import gplx.xowa.wikis.pages.tags.*;
import gplx.dbs.*;
class Xofulltext_searcher_html extends Xow_special_wtr__base {
	private final    byte[] query;
	private final    boolean case_match, auto_wildcard_bgn, auto_wildcard_end, expand_matches_section, show_all_matches;
	private final    int max_pages_per_wiki;
	private final    String wikis, namespaces;
	public Xofulltext_searcher_html
		( byte[] query, boolean case_match, boolean auto_wildcard_bgn, boolean auto_wildcard_end
		, boolean expand_matches_section, boolean show_all_matches
		, int max_pages_per_wiki
		, String wikis, String namespaces) {
		this.query = query;
		this.case_match = case_match;
		this.auto_wildcard_bgn = auto_wildcard_bgn;
		this.auto_wildcard_end = auto_wildcard_end;
		this.expand_matches_section = expand_matches_section;
		this.show_all_matches = show_all_matches;
		this.max_pages_per_wiki = max_pages_per_wiki;
		this.wikis = wikis;
		this.namespaces = namespaces;
	}
	@Override protected Io_url Get_addon_dir(Xoa_app app)			{return Addon_dir(app);}
	@Override protected Io_url Get_mustache_fil(Io_url addon_dir)	{return addon_dir.GenSubFil_nest("bin", "xofulltext_searcher.template.html");}
	@Override protected Mustache_doc_itm Bld_mustache_root(Xoa_app app) {
		return new Xofulltext_searcher_doc
		( query, case_match, auto_wildcard_bgn, auto_wildcard_end
		, expand_matches_section, show_all_matches
		, max_pages_per_wiki, wikis, namespaces);
	}
	@Override protected void Bld_tags(Xoa_app app, Io_url addon_dir, Xopage_html_data page_data) {
		Xopg_tag_mgr head_tags = page_data.Head_tags();
		Xopg_tag_wtr_.Add__xoelem	(head_tags, app.Fsys_mgr().Http_root());
		Xopg_tag_wtr_.Add__xocss	(head_tags, app.Fsys_mgr().Http_root());
		Xopg_tag_wtr_.Add__xohelp	(head_tags, app.Fsys_mgr().Http_root());
		Xopg_tag_wtr_.Add__xolog	(head_tags, app.Fsys_mgr().Http_root());
		Xopg_tag_wtr_.Add__xoajax	(head_tags, app.Fsys_mgr().Http_root(), app);
		Xopg_tag_wtr_.Add__jquery	(head_tags, app.Fsys_mgr().Http_root());
		Xopg_tag_wtr_.Add__xonotify (head_tags, app.Fsys_mgr().Http_root());
		Xopg_alertify_.Add_tags	    (head_tags, app.Fsys_mgr().Http_root());

		head_tags.Add(Xopg_tag_itm.New_css_file(addon_dir.GenSubFil_nest("bin", "xofulltext_searcher.css")));
		head_tags.Add(Xopg_tag_itm.New_js_file(addon_dir.GenSubFil_nest("bin", "xofulltext_searcher.js")));

		page_data.Js_enabled_y_();
	}
	public static Io_url Addon_dir(Xoa_app app) {
		return app.Fsys_mgr().Http_root().GenSubDir_nest("bin", "any", "xowa", "addon", "wiki", "fulltext", "searcher");
	}
}
