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
package gplx.xowa.addons.apps.maints.sql_execs.cbks; import gplx.*; import gplx.xowa.*; import gplx.xowa.addons.*; import gplx.xowa.addons.apps.*; import gplx.xowa.addons.apps.maints.*; import gplx.xowa.addons.apps.maints.sql_execs.*;
import gplx.langs.jsons.*;
import gplx.dbs.*; import gplx.xowa.specials.xowa.diags.*;
/*	TODO.XO
	* add backup function to backup db
	* option to output to file
	* run multiple SQL statements (need to change jdbc)
	* print column headers if SELECT
	* print row counts
	* run stored procs? EXEC debug_image 'A.png'
*/
class Xosql_exec_svc {
	private gplx.xowa.guis.cbks.Xog_cbk_trg cbk_trg = gplx.xowa.guis.cbks.Xog_cbk_trg.New(gplx.xowa.addons.apps.maints.sql_execs.specials.Xosql_exec_special.Prototype.Special__meta().Ttl_bry());
	private final    Xoa_app app;
	public Xosql_exec_svc(Xoa_app app) {
		this.app = app;
	}
	public void Exec(Json_nde args) {
		String domain = args.Get_as_str("domain");
		String db = args.Get_as_str("db");
		String sql = args.Get_as_str("sql");

		// get conn
		Db_conn conn = null;
		if (String_.Eq(domain, DOMAIN__APP)) {
			if (String_.Eq(db, DB__USER)) {
				conn = app.User().User_db_mgr().Conn();
			}
		}
		else if (String_.Eq(domain, DOMAIN__FILE)) {
			conn = Db_conn_bldr.Instance.Get_or_fail(Io_url_.new_any_(db));
		}
		else {
			Xow_wiki wiki = String_.Eq(domain, DOMAIN__HOME)
				? wiki = app.User().Wikii()
				: app.Wiki_mgri().Get_by_or_make_init_y(Bry_.new_u8(domain));
			
			// for now, only support core
			if (String_.Eq(db, DB__CORE)) {
				conn = wiki.Data__core_mgr().Db__core().Conn();
			}
		}

		String results = null;
		try {
			// run sql
			// HACK: assume SQL starting with SELECT is a reader
			if (String_.Has_at_bgn(sql, "SELECT ")) {
				// run select
				Bry_bfr bfr = Bry_bfr_.New();
				Object[][] rows = Db_rdr_utl.Load(conn, sql);
				Db_rdr_utl.Write_to_bfr(bfr, rows);
				results = bfr.To_str_and_clear();
			}
			else {
				conn.Exec_sql(sql);
				results = "executed";
			}
		} catch (Exception exc) {
			results = Err_.Message_gplx_full(exc);
			results = String_.Replace(results, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;"); // tabs must be escaped, else "bad character in String literal"
		}

		// send results
		app.Gui__cbk_mgr().Send_json(cbk_trg, "xo.sql_exec.results__recv", gplx.core.gfobjs.Gfobj_nde.New()
			.Add_bry("msg_text", Bry_.Escape_html(Bry_.new_u8(results))));	// escape html; EX: "<" -> "&lt;"
	}
	private static final String 
	  DOMAIN__APP  = "[xowa.app]"
	, DOMAIN__HOME = "[xowa.home]"
	, DOMAIN__FILE = "[file]"
	, DB__CORE = "core"
	, DB__USER = "user"
	;
}