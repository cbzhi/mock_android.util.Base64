package android.util;

import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import org.junit.Assert;

public class Base64Test {

  private String decodeString(final String in) throws Exception {
    final byte[] out = Base64.decode(in, 0);
    return new String(out);
  }

  private String encodeToString(final String in, final int flags) throws Exception {
    final String b64 = Base64.encodeToString(in.getBytes(), flags);
    final String dec = decodeString(b64);
    Assert.assertEquals(in, dec);
    return b64;
  }

  private void assertBad(final String in) throws Exception {
    try {
      Base64.decode(in, 0);
      Assert.fail("should have failed to decode");
    }
    catch (final IllegalArgumentException ignore) {}
  }

  private void assertEquals(final byte[] expected, final int len, final byte[] actual) {
    Assert.assertEquals(len, actual.length);
    for (int i=0; i<len; ++i) {
      Assert.assertEquals(expected[i], actual[i]);
    }
  }

  private void assertEquals(final byte[] expected, final int len, final byte[] actual, final int alen) {
    Assert.assertEquals(len, alen);
    for (int i=0; i<len; ++i) {
      Assert.assertEquals(expected[i], actual[i]);
    }
  }

  private void assertEquals(final byte[] expected, final byte[] actual) {
    Assert.assertEquals(expected.length, actual.length);
    for (int i=0; i<expected.length; ++i) {
      Assert.assertEquals(expected[i], actual[i]);
    }
  }

  @Test
  public void testDecodeExtraChars() throws Exception {
    Assert.assertEquals("hello, world", decodeString("aGVsbG8sIHdvcmxk"));
    assertBad("aGVsbG8sIHdvcmxk=");
    assertBad("aGVsbG8sIHdvcmxk==");
    assertBad("aGVsbG8sIHdvcmxk =");
    assertBad("aGVsbG8sIHdvcmxk = = ");
    Assert.assertEquals("hello, world", decodeString(" aGVs bG8s IHdv cmxk  "));
    Assert.assertEquals("hello, world", decodeString(" aGV sbG8 sIHd vcmx k "));
    Assert.assertEquals("hello, world", decodeString(" aG VsbG 8sIH dvcm xk "));
    Assert.assertEquals("hello, world", decodeString(" a GVsb G8sI Hdvc mxk "));
    Assert.assertEquals("hello, world", decodeString(" a G V s b G 8 s I H d v c m x k "));
    Assert.assertEquals("hello, world", decodeString("_a*G_V*s_b*G_8*s_I*H_d*v_c*m_x*k_"));
    Assert.assertEquals("hello, world", decodeString("aGVsbG8sIHdvcmxk"));
    Assert.assertEquals("hello, world?!", decodeString("aGVsbG8sIHdvcmxkPyE="));
    Assert.assertEquals("hello, world?!", decodeString("aGVsbG8sIHdvcmxkPyE"));
    assertBad("aGVsbG8sIHdvcmxkPyE==");
    assertBad("aGVsbG8sIHdvcmxkPyE ==");
    assertBad("aGVsbG8sIHdvcmxkPyE = = ");
    Assert.assertEquals("hello, world?!", decodeString("aGVsbG8sIHdvcmxkPy E="));
    Assert.assertEquals("hello, world?!", decodeString("aGVsbG8sIHdvcmxkPy E"));
    Assert.assertEquals("hello, world?!", decodeString("aGVsbG8sIHdvcmxkPy E ="));
    Assert.assertEquals("hello, world?!", decodeString("aGVsbG8sIHdvcmxkPy E "));
    Assert.assertEquals("hello, world?!", decodeString("aGVsbG8sIHdvcmxkPy E = "));
    Assert.assertEquals("hello, world?!", decodeString("aGVsbG8sIHdvcmxkPy E   "));
    Assert.assertEquals("hello, world.", decodeString("aGVsbG8sIHdvcmxkLg=="));
    Assert.assertEquals("hello, world.", decodeString("aGVsbG8sIHdvcmxkLg"));
    assertBad("aGVsbG8sIHdvcmxkLg=");
    assertBad("aGVsbG8sIHdvcmxkLg =");
    assertBad("aGVsbG8sIHdvcmxkLg = ");
    Assert.assertEquals("hello, world.", decodeString("aGVsbG8sIHdvcmxkL g=="));
    Assert.assertEquals("hello, world.", decodeString("aGVsbG8sIHdvcmxkL g"));
    Assert.assertEquals("hello, world.", decodeString("aGVsbG8sIHdvcmxkL g =="));
    Assert.assertEquals("hello, world.", decodeString("aGVsbG8sIHdvcmxkL g "));
    Assert.assertEquals("hello, world.", decodeString("aGVsbG8sIHdvcmxkL g = = "));
    Assert.assertEquals("hello, world.", decodeString("aGVsbG8sIHdvcmxkL g   "));
  }

  private static final byte[] BYTES = { (byte) 0xff, (byte) 0xee, (byte) 0xdd,
                                        (byte) 0xcc, (byte) 0xbb, (byte) 0xaa,
                                        (byte) 0x99, (byte) 0x88, (byte) 0x77 };

  @Test
  public void testBinaryDecode() throws Exception {
    assertEquals(BYTES, 0, Base64.decode("", 0));
    assertEquals(BYTES, 1, Base64.decode("/w==", 0));
    assertEquals(BYTES, 2, Base64.decode("/+4=", 0));
    assertEquals(BYTES, 3, Base64.decode("/+7d", 0));
    assertEquals(BYTES, 4, Base64.decode("/+7dzA==", 0));
    assertEquals(BYTES, 5, Base64.decode("/+7dzLs=", 0));
    assertEquals(BYTES, 6, Base64.decode("/+7dzLuq", 0));
    assertEquals(BYTES, 7, Base64.decode("/+7dzLuqmQ==", 0));
    assertEquals(BYTES, 8, Base64.decode("/+7dzLuqmYg=", 0));
  }

  @Test
  public void testWebSafe() throws Exception {
    assertEquals(BYTES, 0, Base64.decode("", Base64.URL_SAFE));
    assertEquals(BYTES, 1, Base64.decode("_w==", Base64.URL_SAFE));
    assertEquals(BYTES, 2, Base64.decode("_-4=", Base64.URL_SAFE));
    assertEquals(BYTES, 3, Base64.decode("_-7d", Base64.URL_SAFE));
    assertEquals(BYTES, 4, Base64.decode("_-7dzA==", Base64.URL_SAFE));
    assertEquals(BYTES, 5, Base64.decode("_-7dzLs=", Base64.URL_SAFE));
    assertEquals(BYTES, 6, Base64.decode("_-7dzLuq", Base64.URL_SAFE));
    assertEquals(BYTES, 7, Base64.decode("_-7dzLuqmQ==", Base64.URL_SAFE));
    assertEquals(BYTES, 8, Base64.decode("_-7dzLuqmYg=", Base64.URL_SAFE));
    Assert.assertEquals("",
                        Base64.encodeToString(BYTES, 0, 0, Base64.URL_SAFE));
    Assert.assertEquals("_w==\n",
                        Base64.encodeToString(BYTES, 0, 1, Base64.URL_SAFE));
    Assert.assertEquals("_-4=\n",
                        Base64.encodeToString(BYTES, 0, 2, Base64.URL_SAFE));
    Assert.assertEquals("_-7d\n",
                        Base64.encodeToString(BYTES, 0, 3, Base64.URL_SAFE));
    Assert.assertEquals("_-7dzA==\n",
                        Base64.encodeToString(BYTES, 0, 4, Base64.URL_SAFE));
    Assert.assertEquals("_-7dzLs=\n",
                        Base64.encodeToString(BYTES, 0, 5, Base64.URL_SAFE));
    Assert.assertEquals("_-7dzLuq\n",
                        Base64.encodeToString(BYTES, 0, 6, Base64.URL_SAFE));
    Assert.assertEquals("_-7dzLuqmQ==\n",
                        Base64.encodeToString(BYTES, 0, 7, Base64.URL_SAFE));
    Assert.assertEquals("_-7dzLuqmYg=\n",
                        Base64.encodeToString(BYTES, 0, 8, Base64.URL_SAFE));
  }

  @Test
  public void testFlags() throws Exception {
    Assert.assertEquals("YQ==\n",
                        encodeToString("a", 0));
    Assert.assertEquals("YQ==",
                        encodeToString("a", Base64.NO_WRAP));
    Assert.assertEquals("YQ\n",
                        encodeToString("a", Base64.NO_PADDING));
    Assert.assertEquals("YQ",
                        encodeToString("a", Base64.NO_PADDING | Base64.NO_WRAP));
    Assert.assertEquals("YQ==\r\n",
                        encodeToString("a", Base64.CRLF));
    Assert.assertEquals("YQ\r\n",
                        encodeToString("a", Base64.CRLF | Base64.NO_PADDING));
    Assert.assertEquals("YWI=\n",
                        encodeToString("ab", 0));
    Assert.assertEquals("YWI=",
                        encodeToString("ab", Base64.NO_WRAP));
    Assert.assertEquals("YWI\n",
                        encodeToString("ab", Base64.NO_PADDING));
    Assert.assertEquals("YWI",
                        encodeToString("ab", Base64.NO_PADDING | Base64.NO_WRAP));
    Assert.assertEquals("YWI=\r\n",
                        encodeToString("ab", Base64.CRLF));
    Assert.assertEquals("YWI\r\n",
                        encodeToString("ab", Base64.CRLF | Base64.NO_PADDING));
    Assert.assertEquals("YWJj\n",
                        encodeToString("abc", 0));
    Assert.assertEquals("YWJj",
                        encodeToString("abc", Base64.NO_WRAP));
    Assert.assertEquals("YWJj\n",
                        encodeToString("abc", Base64.NO_PADDING));
    Assert.assertEquals("YWJj",
                        encodeToString("abc", Base64.NO_PADDING | Base64.NO_WRAP));
    Assert.assertEquals("YWJj\r\n",
                        encodeToString("abc", Base64.CRLF));
    Assert.assertEquals("YWJj\r\n",
                        encodeToString("abc", Base64.CRLF | Base64.NO_PADDING));
    Assert.assertEquals("YWJjZA==\n",
                        encodeToString("abcd", 0));
    Assert.assertEquals("YWJjZA==",
                        encodeToString("abcd", Base64.NO_WRAP));
    Assert.assertEquals("YWJjZA\n",
                        encodeToString("abcd", Base64.NO_PADDING));
    Assert.assertEquals("YWJjZA",
                        encodeToString("abcd", Base64.NO_PADDING | Base64.NO_WRAP));
    Assert.assertEquals("YWJjZA==\r\n",
                        encodeToString("abcd", Base64.CRLF));
    Assert.assertEquals("YWJjZA\r\n",
                        encodeToString("abcd", Base64.CRLF | Base64.NO_PADDING));
  }

  @Test
  public void testLineLength() throws Exception {
    final String in_56 = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcd";
    final String in_57 = in_56 + "e";
    final String in_58 = in_56 + "ef";
    final String in_59 = in_56 + "efg";
    final String in_60 = in_56 + "efgh";
    final String in_61 = in_56 + "efghi";
    final String prefix = "YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXphYmNkZWZnaGlqa2xtbm9wcXJzdHV2d3h5emFi";
    final String out_56 = prefix + "Y2Q=\n";
    final String out_57 = prefix + "Y2Rl\n";
    final String out_58 = prefix + "Y2Rl\nZg==\n";
    final String out_59 = prefix + "Y2Rl\nZmc=\n";
    final String out_60 = prefix + "Y2Rl\nZmdo\n";
    final String out_61 = prefix + "Y2Rl\nZmdoaQ==\n";
    Assert.assertEquals("", encodeToString("", 0));
    Assert.assertEquals(out_56, encodeToString(in_56, 0));
    Assert.assertEquals(out_57, encodeToString(in_57, 0));
    Assert.assertEquals(out_58, encodeToString(in_58, 0));
    Assert.assertEquals(out_59, encodeToString(in_59, 0));
    Assert.assertEquals(out_60, encodeToString(in_60, 0));
    Assert.assertEquals(out_61, encodeToString(in_61, 0));
    Assert.assertEquals(out_56.replaceAll("=", ""),
                        encodeToString(in_56, Base64.NO_PADDING));
    Assert.assertEquals(out_57.replaceAll("=", ""),
                        encodeToString(in_57, Base64.NO_PADDING));
    Assert.assertEquals(out_58.replaceAll("=", ""),
                        encodeToString(in_58, Base64.NO_PADDING));
    Assert.assertEquals(out_59.replaceAll("=", ""),
                        encodeToString(in_59, Base64.NO_PADDING));
    Assert.assertEquals(out_60.replaceAll("=", ""),
                        encodeToString(in_60, Base64.NO_PADDING));
    Assert.assertEquals(out_61.replaceAll("=", ""),
                        encodeToString(in_61, Base64.NO_PADDING));
    Assert.assertEquals(out_56.replaceAll("\n", ""),
                        encodeToString(in_56, Base64.NO_WRAP));
    Assert.assertEquals(out_57.replaceAll("\n", ""),
                        encodeToString(in_57, Base64.NO_WRAP));
    Assert.assertEquals(out_58.replaceAll("\n", ""),
                        encodeToString(in_58, Base64.NO_WRAP));
    Assert.assertEquals(out_59.replaceAll("\n", ""),
                        encodeToString(in_59, Base64.NO_WRAP));
    Assert.assertEquals(out_60.replaceAll("\n", ""),
                        encodeToString(in_60, Base64.NO_WRAP));
    Assert.assertEquals(out_61.replaceAll("\n", ""),
                        encodeToString(in_61, Base64.NO_WRAP));
  }

  @Test
  public void XXXtestEncodeInternal() throws Exception {
    final byte[] input = { (byte)0x61, (byte)0x62, (byte)0x63 };
    final byte[] output = new byte[100];
    final Base64.Encoder encoder = new Base64.Encoder(Base64.NO_PADDING | Base64.NO_WRAP, output);
    encoder.process(input, 0, 3, false);
    assertEquals("YWJj".getBytes(), 4, encoder.output, encoder.op);
    Assert.assertEquals(0, encoder.tailLen);
    encoder.process(input, 0, 3, false);
    assertEquals("YWJj".getBytes(), 4, encoder.output, encoder.op);
    Assert.assertEquals(0, encoder.tailLen);
    encoder.process(input, 0, 1, false);
    Assert.assertEquals(0, encoder.op);
    Assert.assertEquals(1, encoder.tailLen);
    encoder.process(input, 0, 1, false);
    Assert.assertEquals(0, encoder.op);
    Assert.assertEquals(2, encoder.tailLen);
    encoder.process(input, 0, 1, false);
    assertEquals("YWFh".getBytes(), 4, encoder.output, encoder.op);
    Assert.assertEquals(0, encoder.tailLen);
    encoder.process(input, 0, 2, false);
    Assert.assertEquals(0, encoder.op);
    Assert.assertEquals(2, encoder.tailLen);
    encoder.process(input, 0, 2, false);
    assertEquals("YWJh".getBytes(), 4, encoder.output, encoder.op);
    Assert.assertEquals(1, encoder.tailLen);
    encoder.process(input, 0, 2, false);
    assertEquals("YmFi".getBytes(), 4, encoder.output, encoder.op);
    Assert.assertEquals(0, encoder.tailLen);
    encoder.process(input, 0, 1, true);
    assertEquals("YQ".getBytes(), 2, encoder.output, encoder.op);
  }

  private static final String lipsum =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
    "Quisque congue eleifend odio, eu ornare nulla facilisis eget. " +
    "Integer eget elit diam, sit amet laoreet nibh. Quisque enim " +
    "urna, pharetra vitae consequat eget, adipiscing eu ante. " +
    "Aliquam venenatis arcu nec nibh imperdiet tempor. In id dui " +
    "eget lorem aliquam rutrum vel vitae eros. In placerat ornare " +
    "pretium. Curabitur non fringilla mi. Fusce ultricies, turpis " +
    "eu ultrices suscipit, ligula nisi consectetur eros, dapibus " +
    "aliquet dui sapien a turpis. Donec ultricies varius ligula, " +
    "ut hendrerit arcu malesuada at. Praesent sed elit pretium " +
    "eros luctus gravida. In ac dolor lorem. Cras condimentum " +
    "convallis elementum. Phasellus vel felis in nulla ultrices " +
    "venenatis. Nam non tortor non orci convallis convallis. " +
    "Nam tristique lacinia hendrerit. Pellentesque habitant morbi " +
    "tristique senectus et netus et malesuada fames ac turpis " +
    "egestas. Vivamus cursus, nibh eu imperdiet porta, magna " +
    "ipsum mollis mauris, sit amet fringilla mi nisl eu mi. " +
    "Phasellus posuere, leo at ultricies vehicula, massa risus " +
    "volutpat sapien, eu tincidunt diam ipsum eget nulla. Cras " +
    "molestie dapibus commodo. Ut vel tellus at massa gravida " +
    "semper non sed orci.";

  @Test
  @SuppressWarnings("Duplicates")
  public void testInputStream() throws Exception {
    final int[] flagses = { Base64.DEFAULT,
                            Base64.NO_PADDING,
                            Base64.NO_WRAP,
                            Base64.NO_PADDING | Base64.NO_WRAP,
                            Base64.CRLF,
                            Base64.URL_SAFE };
    final int[] writeLengths = { -10, -5, -1, 0, 1, 1, 2, 2, 3, 10, 100 };
    final Random rng = new Random(32176L);
    final byte[] plain = (lipsum + lipsum + lipsum + lipsum + lipsum).getBytes();
    for (final int flags: flagses) {
      final byte[] encoded = Base64.encode(plain, flags);
      ByteArrayInputStream bais;
      Base64InputStream b64is;
      final byte[] actual = new byte[plain.length * 2];
      int ap;
      int b;
      bais = new ByteArrayInputStream(encoded);
      b64is = new Base64InputStream(bais, flags);
      ap = 0;
      while ((b = b64is.read(actual, ap, actual.length-ap)) != -1) {
        ap += b;
      }
      assertEquals(actual, ap, plain);
      bais = new ByteArrayInputStream(encoded);
      b64is = new Base64InputStream(bais, flags);
      ap = 0;
      while ((b = b64is.read()) != -1) {
        actual[ap++] = (byte) b;
      }
      assertEquals(actual, ap, plain);
      bais = new ByteArrayInputStream(encoded);
      b64is = new Base64InputStream(bais, flags);
      ap = 0;
      readloop: while (true) {
        int l = writeLengths[rng.nextInt(writeLengths.length)];
        if (l >= 0) {
          b = b64is.read(actual, ap, l);
          if (b == -1) break;
          ap += b;
        }
        else {
          for (int i=0; i<-l; ++i) {
            if ((b = b64is.read()) == -1) break readloop;
            actual[ap++] = (byte) b;
          }
        }
      }
      assertEquals(actual, ap, plain);
      bais = new ByteArrayInputStream(plain);
      b64is = new Base64InputStream(bais, flags, true);
      ap = 0;
      while ((b = b64is.read(actual, ap, actual.length-ap)) != -1) {
        ap += b;
      }
      assertEquals(actual, ap, encoded);
      bais = new ByteArrayInputStream(plain);
      b64is = new Base64InputStream(bais, flags, true);
      ap = 0;
      while ((b = b64is.read()) != -1) {
        actual[ap++] = (byte) b;
      }
      assertEquals(actual, ap, encoded);
      bais = new ByteArrayInputStream(plain);
      b64is = new Base64InputStream(bais, flags, true);
      ap = 0;
      readloop: while (true) {
        int l = writeLengths[rng.nextInt(writeLengths.length)];
        if (l >= 0) {
          b = b64is.read(actual, ap, l);
          if (b == -1) break;
          ap += b;
        } else {
          for (int i=0; i<-l; ++i) {
            if ((b = b64is.read()) == -1) break readloop;
            actual[ap++] = (byte) b;
          }
        }
      }
      assertEquals(actual, ap, encoded);
    }
  }

  @Test
  public void testSingleByteReads() throws IOException {
    final InputStream in = new Base64InputStream(
      new ByteArrayInputStream("/v8=".getBytes()), Base64.DEFAULT);
    Assert.assertEquals(254, in.read());
    Assert.assertEquals(255, in.read());
  }

  @Test
  @SuppressWarnings("Duplicates")
  public void testOutputStream() throws Exception {
    final int[] flagses = { Base64.DEFAULT,
                            Base64.NO_PADDING,
                            Base64.NO_WRAP,
                            Base64.NO_PADDING | Base64.NO_WRAP,
                            Base64.CRLF,
                            Base64.URL_SAFE };
    final int[] writeLengths = { -10, -5, -1, 0, 1, 1, 2, 2, 3, 10, 100 };
    final Random rng = new Random(32176L);
    final byte[] plain = (lipsum + lipsum).getBytes();
    for (final int flags: flagses) {
      final byte[] encoded = Base64.encode(plain, flags);
      ByteArrayOutputStream baos;
      Base64OutputStream b64os;
      byte[] actual;
      int p;
      baos = new ByteArrayOutputStream();
      b64os = new Base64OutputStream(baos, flags);
      b64os.write(plain);
      b64os.close();
      actual = baos.toByteArray();
      assertEquals(encoded, actual);
      baos = new ByteArrayOutputStream();
      b64os = new Base64OutputStream(baos, flags);
      for (final byte b : plain) {
        b64os.write(b);
      }
      b64os.close();
      actual = baos.toByteArray();
      assertEquals(encoded, actual);
      baos = new ByteArrayOutputStream();
      b64os = new Base64OutputStream(baos, flags);
      p = 0;
      while (p < plain.length) {
        int l = writeLengths[rng.nextInt(writeLengths.length)];
        l = Math.min(l, plain.length-p);
        if (l >= 0) {
          b64os.write(plain, p, l);
          p += l;
        } else {
          l = Math.min(-l, plain.length-p);
          for (int i = 0; i < l; ++i) {
            b64os.write(plain[p+i]);
          }
          p += l;
        }
      }
      b64os.close();
      actual = baos.toByteArray();
      assertEquals(encoded, actual);
      baos = new ByteArrayOutputStream();
      b64os = new Base64OutputStream(baos, flags, false);
      b64os.write(encoded);
      b64os.close();
      actual = baos.toByteArray();
      assertEquals(plain, actual);
      baos = new ByteArrayOutputStream();
      b64os = new Base64OutputStream(baos, flags, false);
      for (final byte b : encoded) {
        b64os.write(b);
      }
      b64os.close();
      actual = baos.toByteArray();
      assertEquals(plain, actual);
      baos = new ByteArrayOutputStream();
      b64os = new Base64OutputStream(baos, flags, false);
      p = 0;
      while (p < encoded.length) {
        int l = writeLengths[rng.nextInt(writeLengths.length)];
        l = Math.min(l, encoded.length-p);
        if (l >= 0) {
          b64os.write(encoded, p, l);
          p += l;
        } else {
          l = Math.min(-l, encoded.length-p);
          for (int i=0; i<l; ++i) {
            b64os.write(encoded[p+i]);
          }
          p += l;
        }
      }
      b64os.close();
      actual = baos.toByteArray();
      assertEquals(plain, actual);
    }
  }

}
