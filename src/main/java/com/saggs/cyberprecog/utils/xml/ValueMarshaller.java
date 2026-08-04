package com.saggs.cyberprecog.utils.xml;

import java.io.*;

public class ValueMarshaller
{
  static public String normalizeString(String strValue, boolean isAttVal)
    throws IOException
  {
    StringWriter writer = new StringWriter();
    char[] chars = strValue.toCharArray();
    normalizeAndPrint(writer, chars, 0, chars.length, isAttVal);
    return writer.toString();
  }

  static public void normalizeAndPrint(Writer writer, String strValue, boolean isAttVal)
    throws IOException
  {
    char[] chars = strValue.toCharArray();
    normalizeAndPrint(writer, chars, 0, chars.length, isAttVal);
  }

  static public void normalizeAndPrint(Writer writer, char ch[], int start, int length, boolean isAttVal)
  throws IOException
  {
      for (int i = start; i < start + length; i++)
      {
          switch (ch[i])
          {
            case '&':
            {
                writer.write("&amp;");
                break;
            }
            case '<':
            {
                writer.write("&lt;");
                break;
            }
            case '>':
            {
                writer.write("&gt;");
                break;
            }
            case '\"':
            {
                if (isAttVal)
                {
                    writer.write("&quot;");
                }
                else
                {
                    writer.write('\"');
                }
                break;
            }
            default:
            {
                if (ch[i] > '\u007f')
                {
                    writer.write("&#");
                    writer.write(Integer.toString(ch[i]));
                    writer.write(';');
                }
                else
                {
                    writer.write(ch[i]);
                }
            }
        }
      }
  }

}