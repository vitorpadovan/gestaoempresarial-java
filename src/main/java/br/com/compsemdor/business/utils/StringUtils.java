package br.com.compsemdor.business.utils;

import java.text.ParseException;

import javax.swing.text.MaskFormatter;

import br.com.compsemdor.controller.exceptions.AplicationException;

public class StringUtils {

	private StringUtils() {
	}

	public static String removeSymbols(String string) {
		string = string.replace('-', ' ');
		string = string.replace('.', ' ');
		string = string.replace('\\', ' ');
		string = string.replace('/', ' ');
		string = string.replace(';', ' ');
		string = string.replace(',', ' ');
		string = string.trim();
		string = string.replaceAll(" ", "");
		return string;
	}

	public static String formatMask(String text, String format, char placeholder) {
		try {
			MaskFormatter mf = new MaskFormatter(format);
			mf.setPlaceholderCharacter(placeholder);
			mf.setValueContainsLiteralCharacters(false);
			return mf.valueToString(text);
		} catch (ParseException e) {
			throw new AplicationException("Erro ao converter",
					String.format("Erro ao converter o texto '%s' para a mascara '%s'. Erro: %s ", e.getMessage(), text,
							format));
		}
	}

	public static String formatMask(String text, String format) {
		return StringUtils.formatMask(text, format, '#');
	}
}
