package com.solab.iso8583.codecs;

import com.solab.iso8583.CustomBinaryField;
import com.solab.iso8583.CustomField;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.parse.FieldParseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * A codec to manage subfields inside a field of a certain type.
 *
 * @author Enrique Zamudio
 *         Date: 25/11/13 11:25
 */
public class CompositeField implements CustomBinaryField<CompositeField> {

    private static final Logger log = LoggerFactory.getLogger(CompositeField.class);

    static final byte[] HEX = new byte[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private String encoding = System.getProperty("file.encoding");

    /** Stores the subfields. */
    @SuppressWarnings("rawtypes")
    //private List<IsoValue> values;
    private IsoValue[] values = new IsoValue[129]; // Modified by Lekan Omotayo
    /** Stores the parsers for the subfields. */
    //private List<FieldParseInfo> parsers;
    private FieldParseInfo[] parsers;

    List<Integer> index;// Modified by Lekan Omotayo

    /** Flag to specify if missing fields should be ignored as long as they're at
     * the end of the message. */
    private boolean ignoreLast; // Modified by Lekan Omotayo
    /** Flag to enforce secondary bitmap even if empty. */
    private boolean forceb2; // Modified by Lekan Omotayo
    private boolean binBitmap; // Modified by Lekan Omotayo
    private boolean forceStringEncoding; // Modified by Lekan Omotayo
    /** Indicates if the message is binary-coded. */
    private boolean binary; // Modified by Lekan Omotayo

    public CompositeField(){

    }

    @SuppressWarnings("rawtypes")
    /*public void setValues(List<IsoValue> values) {
        this.values = values;
    }*/
    public void setValues(IsoValue[] values) {
        this.values = values;
    } // Modified by Lekan Omotayo
    @SuppressWarnings("rawtypes")
    /*public List<IsoValue> getValues() {
        return values;
    }
    */
    public IsoValue[] getValues() {
        return values;
    } // Modified by Lekan Omotayo
    @SuppressWarnings("rawtypes")
    /*
    public CompositeField addValue(IsoValue<?> v) {
        if (values == null) {
            values = new ArrayList<IsoValue>(4);
        }
        values.add(v);
        return this;
    }
    */
    public CompositeField addValue(int index, IsoValue<?> v) { // Modified by Lekan Omotayo
        if (index < 1 || index > 128) {
            throw new IndexOutOfBoundsException("Field index must be between 1 and 128");
        }
        //if (v != null) {
        //    v.setCharacterEncoding(encoding);
        //}
        values[index] = v;
        return this;
    }

    /*
    public <T> CompositeField addValue(T val, CustomField<T> encoder, IsoType t, int length) {
        return addValue(t.needsLength() ? new IsoValue<T>(t, val, length, encoder)
                : new IsoValue<T>(t, val, encoder));
    }*/
    public <T> CompositeField addValue(int index, T val, CustomField<T> encoder, IsoType t, int length) { // Modified by Lekan Omotayo
        return addValue(index, t.needsLength() ? new IsoValue<T>(t, val, length, encoder)
                : new IsoValue<T>(t, val, encoder));
    }

    @SuppressWarnings("unchecked")
    /*public <T> IsoValue<T> getField(int idx) {
        if (idx < 0 || idx > values.size())return null;
        return values.get(idx);
    }*/
    public <T> IsoValue<T> getField(int field) {
        return values[field];
    } // Modified by Lekan Omotayo

    public <T> T getObjectValue(int idx) {
        IsoValue<T> v = getField(idx);
        return v==null ? null : v.getValue();
    }

    public List<Integer> getIndex() {
        return index;
    }

    public void setIndex(List<Integer> index) {
        this.index = index;
    }

    public CompositeField addIndex(int i) { // Modified by Lekan Omotayo
        if (index == null) {
            index = new ArrayList<Integer>(4);
        }

        index.add(i);
        return this;
    }


    public void setParsers(FieldParseInfo[] fpis) {
        parsers = fpis;
    }
    public FieldParseInfo[] getParsers() {
        return parsers;
    }
    public CompositeField addParser(int index, FieldParseInfo fpi) { // Modified by Lekan Omotayo
        if (parsers == null) {
            parsers = new FieldParseInfo[128];
        }
        parsers[index] = fpi;
        return this;
    }

    @Override
    public CompositeField decodeBinaryField(byte[] buf, int offset, int length) { // Modified by Lekan Omotayo
        @SuppressWarnings("rawtypes")
        //List<IsoValue> vals = new ArrayList<IsoValue>(parsers.size());
        IsoValue[] vals = new IsoValue[129];

        final int minlength = 8;
        if (buf.length < minlength) {
            //throw new ParseException("Insufficient buffer length, needs to be at least " + minlength, 0);
            log.error("Insufficient buffer length, needs to be at least ", new ParseException("Insufficient buffer length, needs to be at least " + minlength, 0));
            return null;
        }
        //Parse the bitmap (primary first)
        final BitSet bs = new BitSet(64);
        int pos = 0;

        //if (binary || binBitmap) {
            final int bitmapStart = (binary ? 2 : 4);
            for (int i = bitmapStart; i < 8+bitmapStart; i++) {
                int bit = 128;
                for (int b = 0; b < 8; b++) {
                    bs.set(pos++, (buf[i] & bit) != 0);
                    bit >>= 1;
                }
            }
            //Check for secondary bitmap and parse if necessary
            if (bs.get(0)) {
                if (buf.length < minlength + 8) {
                    //throw new ParseException("Insufficient length for secondary bitmap", minlength);
                    log.error("Insufficient length for secondary bitmap", new ParseException("Insufficient length for secondary bitmap", minlength));
                    return null;
                }
                for (int i = 8+bitmapStart; i < 16+bitmapStart; i++) {
                    int bit = 128;
                    for (int b = 0; b < 8; b++) {
                        bs.set(pos++, (buf[i] & bit) != 0);
                        bit >>= 1;
                    }
                }
                pos = minlength + 8;
            } else {
                pos = minlength;
            }
        //}

        boolean abandon = false;
        for (int i = 1; i < bs.length(); i++) {
            if (bs.get(i) && !index.contains(i+1)) {
                log.warn("ISO8583 MessageFactory cannot parse field {}: unspecified in parsing guide", i+1);
                abandon = true;
            }
        }
        if (abandon) {
            //throw new ParseException("ISO8583 MessageFactory cannot parse fields", 0);
            log.error("ISO8583 MessageFactory cannot parse fields", new ParseException("ISO8583 MessageFactory cannot parse fields", 0));
            return null;
        }

        for (Integer i : index) {
            FieldParseInfo fpi = parsers[i];
            if (bs.get(i - 1)) {
                if (ignoreLast && pos >= buf.length && i.intValue() == index.get(index.size() -1)) {
                    log.warn("Field {} is not really in the message even though it's in the bitmap", i);
                    bs.clear(i - 1);
                } else {

                    try{
                        IsoValue<?> v = fpi.parseBinary(i, buf, pos, null);
                        if (v != null) {
                            if (v.getType() == IsoType.NUMERIC || v.getType() == IsoType.DATE10
                                    || v.getType() == IsoType.DATE4 || v.getType() == IsoType.DATE_EXP
                                    || v.getType() == IsoType.AMOUNT || v.getType() == IsoType.TIME) {
                                pos += (v.getLength() / 2) + (v.getLength() % 2);
                            } else {
                                pos += v.getLength();
                            }
                            if (v.getType() == IsoType.LLVAR || v.getType() == IsoType.LLBIN) {
                                pos++;
                            } else if (v.getType() == IsoType.LLLVAR || v.getType() == IsoType.LLLBIN
                                    || v.getType() == IsoType.LLLLVAR || v.getType() == IsoType.LLLLBIN) {
                                pos+=2;
                            } else if (v.getType() == IsoType.LLLLLVAR || v.getType() == IsoType.LLLLLLVAR) {
                                pos+=3;
                            }
                            //vals.add(v);
                            vals[i++] = v;
                        }
                    } catch (ParseException ex) {
                        log.error("Decoding binary CompositeField", ex);
                        return null;
                    } catch (UnsupportedEncodingException ex) {
                        log.error("Decoding binary CompositeField", ex);
                        return null;
                    }
                }
            }
        }
        final CompositeField f = new CompositeField();
        f.setValues(vals);
        return f;

        /*
        //int pos = 0;
        try {
            int i = 0;
            for (FieldParseInfo fpi : parsers) {
                IsoValue<?> v = fpi.parseBinary(0, buf, pos, null);
                if (v != null) {
                    if (v.getType() == IsoType.NUMERIC || v.getType() == IsoType.DATE10
                            || v.getType() == IsoType.DATE4 || v.getType() == IsoType.DATE_EXP
                            || v.getType() == IsoType.AMOUNT || v.getType() == IsoType.TIME) {
                        pos += (v.getLength() / 2) + (v.getLength() % 2);
                    } else {
                        pos += v.getLength();
                    }
                    if (v.getType() == IsoType.LLVAR || v.getType() == IsoType.LLBIN) {
                        pos++;
                    } else if (v.getType() == IsoType.LLLVAR || v.getType() == IsoType.LLLBIN) {
                        pos+=2;
                    }
                    else if (v.getType() == IsoType.LLLLLLVAR) {
                        pos+=3;
                    }
                    //vals.add(v);
                    vals[i++] = v;
                }
            }

            final CompositeField f = new CompositeField();
            f.setValues(vals);
            return f;
        } catch (ParseException ex) {
            log.error("Decoding binary CompositeField", ex);
            return null;
        } catch (UnsupportedEncodingException ex) {
            log.error("Decoding binary CompositeField", ex);
            return null;
        }
        */
    }


    @Override
    public CompositeField decodeField(String value) { // Modified by Lekan Omotayo
        @SuppressWarnings("rawtypes")
        //List<IsoValue> vals = new ArrayList<IsoValue>(parsers.size());
        IsoValue[] vals = new IsoValue[129];
        byte[] buf = value.getBytes();

        //final int minlength = (binary?2:4)+(binBitmap||binary ? 8:16);
        final int minlength = 16;
        if (buf.length < minlength) {
          //  throw new ParseException("Insufficient buffer length, needs to be at least " + minlength, 0);
            log.error("Insufficient buffer length, needs to be at least " + minlength, 0);
            return null;
        }
        //Parse the bitmap (primary first)
        final BitSet bs = new BitSet(64);
        int pos = 0;

         //else {
            //ASCII parsing
            try {
                final byte[] bitmapBuffer;
                if (forceStringEncoding) {
                    byte[] _bb = new String(buf, 0, 16, encoding).getBytes();
                    bitmapBuffer = new byte[32]; // 36?
                    System.arraycopy(_bb, 0, bitmapBuffer, 0, 16);
                } else {
                    bitmapBuffer = buf;
                }
                for (int i = 0; i < 16; i++) {
                    if (bitmapBuffer[i] >= '0' && bitmapBuffer[i] <= '9') {
                        bs.set(pos++, ((bitmapBuffer[i] - 48) & 8) > 0);
                        bs.set(pos++, ((bitmapBuffer[i] - 48) & 4) > 0);
                        bs.set(pos++, ((bitmapBuffer[i] - 48) & 2) > 0);
                        bs.set(pos++, ((bitmapBuffer[i] - 48) & 1) > 0);
                    } else if (bitmapBuffer[i] >= 'A' && bitmapBuffer[i] <= 'F') {
                        bs.set(pos++, ((bitmapBuffer[i] - 55) & 8) > 0);
                        bs.set(pos++, ((bitmapBuffer[i] - 55) & 4) > 0);
                        bs.set(pos++, ((bitmapBuffer[i] - 55) & 2) > 0);
                        bs.set(pos++, ((bitmapBuffer[i] - 55) & 1) > 0);
                    } else if (bitmapBuffer[i] >= 'a' && bitmapBuffer[i] <= 'f') {
                        bs.set(pos++, ((bitmapBuffer[i] - 87) & 8) > 0);
                        bs.set(pos++, ((bitmapBuffer[i] - 87) & 4) > 0);
                        bs.set(pos++, ((bitmapBuffer[i] - 87) & 2) > 0);
                        bs.set(pos++, ((bitmapBuffer[i] - 87) & 1) > 0);
                    }
                }
                //Check for secondary bitmap and parse it if necessary
                if (bs.get(0)) {
                    if (buf.length < minlength + 16) {
                    //    throw new ParseException("Insufficient length for secondary bitmap", minlength);
                        log.error("Insufficient length for secondary bitmap", minlength);
                        return null;
                    }
                    if (forceStringEncoding) {
                        byte[] _bb = new String(buf, 16, 16, encoding).getBytes();
                        System.arraycopy(_bb, 0, bitmapBuffer, 16, 16);
                    }
                    for (int i = 16; i < 32; i++) {
                        if (bitmapBuffer[i] >= '0' && bitmapBuffer[i] <= '9') {
                            bs.set(pos++, ((bitmapBuffer[i] - 48) & 8) > 0);
                            bs.set(pos++, ((bitmapBuffer[i] - 48) & 4) > 0);
                            bs.set(pos++, ((bitmapBuffer[i] - 48) & 2) > 0);
                            bs.set(pos++, ((bitmapBuffer[i] - 48) & 1) > 0);
                        } else if (bitmapBuffer[i] >= 'A' && bitmapBuffer[i] <= 'F') {
                            bs.set(pos++, ((bitmapBuffer[i] - 55) & 8) > 0);
                            bs.set(pos++, ((bitmapBuffer[i] - 55) & 4) > 0);
                            bs.set(pos++, ((bitmapBuffer[i] - 55) & 2) > 0);
                            bs.set(pos++, ((bitmapBuffer[i] - 55) & 1) > 0);
                        } else if (bitmapBuffer[i] >= 'a' && bitmapBuffer[i] <= 'f') {
                            bs.set(pos++, ((bitmapBuffer[i] - 87) & 8) > 0);
                            bs.set(pos++, ((bitmapBuffer[i] - 87) & 4) > 0);
                            bs.set(pos++, ((bitmapBuffer[i] - 87) & 2) > 0);
                            bs.set(pos++, ((bitmapBuffer[i] - 87) & 1) > 0);
                        }
                    }
                    pos = 16 + minlength;
                } else {
                    pos = minlength;
                }
            } catch (Exception ex) {
                ParseException _e = new ParseException("Invalid ISO8583 bitmap", pos);
                _e.initCause(ex);
                log.error("Decoding CompositeField", ex);
                return null;
                //throw _e;
            }
        //}

        boolean abandon = false;
        for (int i = 1; i < bs.length(); i++) {
            if (bs.get(i) && !index.contains(i+1)) {
                log.warn("ISO8583 MessageFactory cannot parse field {}: unspecified in parsing guide", i+1);
                abandon = true;
            }
        }
        if (abandon) {
            //throw new ParseException("ISO8583 MessageFactory cannot parse fields", 0);
            log.error("ISO8583 MessageFactory cannot parse fields", new ParseException("ISO8583 MessageFactory cannot parse fields", 0));
            return null;
        }

        for (Integer i : index) {
            FieldParseInfo fpi = parsers[i];
            if (bs.get(i - 1)) {
                try{
                    if (ignoreLast && pos >= buf.length && i.intValue() == index.get(index.size() -1)) {
                        log.warn("Field {} is not really in the message even though it's in the bitmap", i);
                        bs.clear(i - 1);
                    } else {
                        IsoValue<?> v = fpi.parse(i, buf, pos, null);
                        if (v != null) {
                            pos += v.toString().getBytes(fpi.getCharacterEncoding()).length;
                            if (v.getType() == IsoType.LLVAR || v.getType() == IsoType.LLBIN) {
                                pos+=2;
                            } else if (v.getType() == IsoType.LLLVAR || v.getType() == IsoType.LLLBIN) {
                                pos+=3;
                            } else if (v.getType() == IsoType.LLLLVAR || v.getType() == IsoType.LLLLBIN) {
                                pos+=4;
                            } else if (v.getType() == IsoType.LLLLLVAR) {
                                pos+=5;
                            } else if (v.getType() == IsoType.LLLLLLVAR) {
                                pos+=6;
                            }
                            //vals.add(v);
                            vals[i++] = v;
                        }
                    }
                }
                catch (ParseException ex) {
                    log.error("Decoding CompositeField", ex);
                    return null;
                } catch (UnsupportedEncodingException ex) {
                    log.error("Decoding CompositeField", ex);
                    return null;
                }
            }
        }

        final CompositeField f = new CompositeField();
        f.setValues(vals);
        return f;

        /*
        //int pos = 0;
        try {
            int i = 0;
            for (FieldParseInfo fpi : parsers) {
                IsoValue<?> v = fpi.parse(0, buf, pos, null);
                if (v != null) {
                    pos += v.toString().getBytes(fpi.getCharacterEncoding()).length;
                    if (v.getType() == IsoType.LLVAR || v.getType() == IsoType.LLBIN) {
                        pos+=2;
                    } else if (v.getType() == IsoType.LLLVAR || v.getType() == IsoType.LLLBIN) {
                        pos+=3;
                    }
                    else if (v.getType() == IsoType.LLLLLLVAR) {
                        pos+=6;
                    }
                    //vals.add(v);
                    vals[i++] = v;
                }
            }
            final CompositeField f = new CompositeField();
            f.setValues(vals);
            return f;
        } catch (ParseException ex) {
            log.error("Decoding CompositeField", ex);
            return null;
        } catch (UnsupportedEncodingException ex) {
            log.error("Decoding CompositeField", ex);
            return null;
        }
        */
    }

    @Override
    public byte[] encodeBinaryField(CompositeField value) { // Modified by Lekan Omotayo
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        writeBitmap(bout);
        try {
            for (IsoValue<?> v : value.getValues()) {
                if(v != null) {
                    v.write(bout, true, true);
                }
            }
        } catch (IOException ex) {
            log.error("Encoding binary CompositeField", ex);
            //shouldn't happen
        }
        return bout.toByteArray();
    }

    @Override
    public String encodeField(CompositeField value) { // Modified by Lekan Omotayo
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        writeBitmap(bout);
        byte[] buf = null;
        try {
            String encoding = null;
            for (IsoValue<?> v : value.getValues()) {
                if(v != null){
                    v.write(bout, false, true);
                    if (encoding == null)encoding = v.getCharacterEncoding();
                }
            }
            buf = bout.toByteArray();
            return new String(buf, encoding==null?"UTF-8":encoding);
        } catch (UnsupportedEncodingException ex) {
            log.error("Encoding text CompositeField", ex);
        } catch (IOException ex) {
            log.error("Encoding text CompositeField", ex);
            //shouldn't happen
        }
        return new String(buf);
    }

    protected void writeBitmap(ByteArrayOutputStream bout){ // Modified by Lekan Omotayo
        //Bitmap
        BitSet bs = createBitmapBitSet();
        //Write bitmap to stream
        if (binary || binBitmap) {
            int pos = 128;
            int b = 0;
            for (int i = 0; i < bs.size(); i++) {
                if (bs.get(i)) {
                    b |= pos;
                }
                pos >>= 1;
                if (pos == 0) {
                    bout.write(b);
                    pos = 128;
                    b = 0;
                }
            }
        } else {
            ByteArrayOutputStream bout2 = null;
            if (forceStringEncoding) {
                bout2 = bout;
                bout = new ByteArrayOutputStream();
            }
            int pos = 0;
            int lim = bs.size() / 4;
            for (int i = 0; i < lim; i++) {
                int nibble = 0;
                if (bs.get(pos++))
                    nibble |= 8;
                if (bs.get(pos++))
                    nibble |= 4;
                if (bs.get(pos++))
                    nibble |= 2;
                if (bs.get(pos++))
                    nibble |= 1;
                bout.write(HEX[nibble]);
            }
            if (forceStringEncoding) {
                final String _hb = new String(bout.toByteArray());
                bout = bout2;
                try {
                    bout.write(_hb.getBytes(encoding));
                } catch (IOException ignore) {
                    //never happen
                }
            }
        }
    }

    /** Creates a BitSet for the bitmap. */
    protected BitSet createBitmapBitSet() { // Modified by Lekan Omotayo
        BitSet bs = new BitSet(forceb2 ? 128 : 64);
        for (int i = 2 ; i < 129; i++) {
            if (values[i] != null) {
                bs.set(i - 1);
            }
        }
        if (forceb2) {
            bs.set(0);
        } else if (bs.length() > 64) {
            //Extend to 128 if needed
            BitSet b2 = new BitSet(128);
            b2.or(bs);
            bs = b2;
            bs.set(0);
        }
        return bs;
    }


    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public boolean isForceb2() {
        return forceb2;
    }

    public void setForceb2(boolean forceb2) {
        this.forceb2 = forceb2;
    }

    public boolean isBinBitmap() {
        return binBitmap;
    }

    public void setBinBitmap(boolean binBitmap) {
        this.binBitmap = binBitmap;
    }

    public boolean isForceStringEncoding() {
        return forceStringEncoding;
    }

    public void setForceStringEncoding(boolean forceStringEncoding) {
        this.forceStringEncoding = forceStringEncoding;
    }

    public boolean isBinary() {
        return binary;
    }

    public void setBinary(boolean binary) {
        this.binary = binary;
    }

    @Override
    public String toString() { // Modified by Lekan Omotayo
        StringBuilder sb = new StringBuilder("CompositeField[");
        if (values!=null) {
            boolean first=true;
            int i = 0;
            for (IsoValue<?> v : values) {
                if(v!= null){
                    //if (first)
                    //    first=false;
                    //else
                    //    sb.append(',');
                    //sb.append(v.getType());
                    sb.append("\n       " + i + " :[" +v.getType().toString() + "(" + v.getLength() + ")] = '" + v.getValue() + "'");
                }
                i++;
            }
        }
        return sb.append(']').toString();
    }
}
