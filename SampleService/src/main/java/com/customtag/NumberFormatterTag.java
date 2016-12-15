package main.java.com.customtag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.mysql.jdbc.StringUtils;

import javax.servlet.jsp.SkipPageException;
import java.text.DecimalFormat;

public class NumberFormatterTag extends SimpleTagSupport{


	private String format;

	private String number;

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}


	public NumberFormatterTag() {
	}

	@Override
	public void doTag() throws JspException, IOException {
		try {
			if(!StringUtils.isEmptyOrWhitespaceOnly(getNumber())){
				double amount = Double.parseDouble(getNumber());
				DecimalFormat formatter = new DecimalFormat(getFormat());
				String formattedNumber = formatter.format(amount);
				getJspContext().getOut().write(formattedNumber);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SkipPageException("Exception in formatting " + number
					+ " with format " + format);
		}
	}

}

