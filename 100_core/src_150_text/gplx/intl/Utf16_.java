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
package gplx.intl; import gplx.*;
public class Utf16_ {		
	public static int Surrogate_merge(int hi, int lo) { // REF: http://perldoc.perl.org/Encode/Unicode.html
		return 0x10000 + (hi - 0xD800) * 0x400 + (lo - 0xDC00);
	}
	public static void Surrogate_split(int v, Int_obj_ref hi, Int_obj_ref lo) {
		hi.Val_((v - 0x10000) / 0x400 + 0xD800);
		lo.Val_((v - 0x10000) % 0x400 + 0xDC00);
	}
	public static int Decode_to_int(byte[] ary, int pos) {
		byte b0 = ary[pos];
		if 		((b0 & 0x80) == 0) {
			return  b0;			
		}
		else if ((b0 & 0xE0) == 0xC0) {
			return  ( b0           & 0x1f) <<  6
				| 	( ary[pos + 1] & 0x3f)
				;			
		}
		else if ((b0 & 0xF0) == 0xE0) {
			return  ( b0           & 0x0f) << 12
				| 	((ary[pos + 1] & 0x3f) <<  6)
				| 	( ary[pos + 2] & 0x3f)
				;			
		}
		else if ((b0 & 0xF8) == 0xF0) {
			return  ( b0           & 0x07) << 18
				| 	((ary[pos + 1] & 0x3f) << 12)
				| 	((ary[pos + 2] & 0x3f) <<  6)
				| 	( ary[pos + 3] & 0x3f)
				;			
		}
		else throw Err_.new_fmt_("invalid utf8 byte: byte={0}", b0);
	}
	public static byte[] Encode_hex_to_bry(String raw) {return Encode_hex_to_bry(Bry_.new_ascii_(raw));}
	public static byte[] Encode_hex_to_bry(byte[] raw) {
		if (raw == null) return null;
		int int_val = gplx.texts.HexDecUtl.parse_or_(raw, Int_.MinValue);
		return int_val == Int_.MinValue ? null : Encode_int_to_bry(int_val);
	}
	public static byte[] Encode_int_to_bry(int c) {
		int bry_len = Len_by_int(c);
		byte[] bry = new byte[bry_len];
		Encode_int(c, bry, 0);
		return bry;
	}
	public static int Encode_char(int c, char[] c_ary, int c_pos, byte[] b_ary, int b_pos) {
		if	   ((c >  -1)
			 && (c < 128)) {
			b_ary[  b_pos]	= (byte)c;
			return 1;
		}
		else if (c < 2048) {
			b_ary[  b_pos] 	= (byte)(0xC0 | (c >>   6));
			b_ary[++b_pos] 	= (byte)(0x80 | (c & 0x3F));
			return 1;
		}	
		else if((c > 55295)				// 0xD800
			 && (c < 56320)) {			// 0xDFFF
			if (c_pos >= c_ary.length) throw Err_.new_fmt_("incomplete surrogate pair at end of String; char={0}", c);
			char nxt_char = c_ary[c_pos + 1];
			int v = Surrogate_merge(c, nxt_char);
			b_ary[b_pos] 	= (byte)(0xF0 | (v >> 18));
			b_ary[++b_pos] 	= (byte)(0x80 | (v >> 12) & 0x3F);
			b_ary[++b_pos] 	= (byte)(0x80 | (v >>  6) & 0x3F);
			b_ary[++b_pos] 	= (byte)(0x80 | (v        & 0x3F));
			return 2;
		}
		else {
			b_ary[b_pos] 	= (byte)(0xE0 | (c >> 12));
			b_ary[++b_pos] 	= (byte)(0x80 | (c >>  6) & 0x3F);
			b_ary[++b_pos] 	= (byte)(0x80 | (c        & 0x3F));
			return 1;
		}
	}
	public static int Encode_int(int c, byte[] src, int pos) {
		if	   ((c > -1)
			 && (c < 128)) {
			src[  pos]	= (byte)c;
			return 1;
		}
		else if (c < 2048) {
			src[  pos] 	= (byte)(0xC0 | (c >>   6));
			src[++pos] 	= (byte)(0x80 | (c & 0x3F));
			return 2;
		}	
		else if (c < 65536) {
			src[pos] 	= (byte)(0xE0 | (c >> 12));
			src[++pos] 	= (byte)(0x80 | (c >>  6) & 0x3F);
			src[++pos] 	= (byte)(0x80 | (c        & 0x3F));
			return 3;
		}
		else if (c < 2097152) {
			src[pos] 	= (byte)(0xF0 | (c >> 18));
			src[++pos] 	= (byte)(0x80 | (c >> 12) & 0x3F);
			src[++pos] 	= (byte)(0x80 | (c >>  6) & 0x3F);
			src[++pos] 	= (byte)(0x80 | (c        & 0x3F));
			return 4;
		}
		else throw Err_.new_fmt_("UTF-16 int must be between 0 and 2097152; char={0}", c);
	}
	private static int Len_by_int(int c) {
		if	   ((c >       -1)
			 && (c <      128))	return 1;		// 1 <<  7
		else if (c <     2048)	return 2;		// 1 << 11
		else if (c <   65536)	return 3;		// 1 << 16
		else if (c < 2097152)	return 4;
		else throw Err_.new_fmt_("UTF-16 int must be between 0 and 2097152; char={0}", c);
	}
	public static int Len_by_char(int c) {
		if	   ((c >       -1)
			 && (c <      128))	return 1;		// 1 <<  7
		else if (c <     2048)	return 2;		// 1 << 11
		else if((c >    55295)					// 0xD800
			 && (c <    56320))	return 4;		// 0xDFFF
		else if (c <    65536)	return 3;		// 1 << 16
		else throw Err_.new_fmt_("UTF-16 int must be between 0 and 65536; char={0}", c);
	}
}
