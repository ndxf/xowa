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
package gplx.xowa.xtns.scribunto.engines.process; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.scribunto.*; import gplx.xowa.xtns.scribunto.engines.*;
import gplx.php.*;
public class Process_recv_msg {
	private Php_srl_parser parser = new Php_srl_parser();
	public Process_recv_msg() {
		arg_keys.Add("op"			, Byte_obj_val.new_(Arg_op));
		arg_keys.Add("values"		, Byte_obj_val.new_(Arg_values));
		arg_keys.Add("id"			, Byte_obj_val.new_(Arg_id));
		arg_keys.Add("args"			, Byte_obj_val.new_(Arg_args));
	}	private HashAdp arg_keys = HashAdp_.new_(); private static final byte Arg_op = 0, Arg_values = 1, Arg_id = 2, Arg_args = 3;
	public String Op() {return op;} private String op;
	public String Call_id() {return call_id;} private String call_id;
	public KeyVal[] Rslt_ary() {return rslt_ary;} private KeyVal[] rslt_ary;
	public KeyVal[] Values() {return values;} private KeyVal[] values;
	public KeyVal[] Call_args() {return call_args;} private KeyVal[] call_args;
	public String Extract(byte[] rsp) {
		try {
			op = call_id = null;
			rslt_ary = values = call_args = null;
			KeyVal[] root_ary = parser.Parse_as_kvs(rsp);
			rslt_ary = (KeyVal[])root_ary[0].Val();
			int len = rslt_ary.length;
			for (int i = 0; i < len; i++) {
				KeyVal kv = rslt_ary[i];
				String kv_key = kv.Key();
				Byte_obj_val bv = (Byte_obj_val)arg_keys.Fetch(kv_key);
				if	(bv != null) {
					switch (bv.Val()) {
						case Arg_op:		op = kv.Val_to_str_or_empty(); break;
						case Arg_values: 	values = (KeyVal[])kv.Val(); break;
						case Arg_id:		call_id = kv.Val_to_str_or_empty(); break;
						case Arg_args:		call_args = (KeyVal[])kv.Val(); break;
					}
				}
			}
			return op;
		}
		catch (Exception e) {
			throw Scrib_xtn_mgr.err_(e, "failed to extract data: {0} {1}", Err_.Message_gplx_brief(e), String_.new_utf8_(rsp));
		}
	}
}
