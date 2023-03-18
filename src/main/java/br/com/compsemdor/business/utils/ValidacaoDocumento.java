package br.com.compsemdor.business.utils;

public class ValidacaoDocumento {

	private ValidacaoDocumento() {
	}

	public static boolean validaCPF(String vrCPF) {
		String valor = vrCPF.replace(".", "");
		valor = valor.replace("-", "");
		if (valor.length() != 11)
			return false;
		boolean igual = true;
		for (int i = 1; i < 11 && igual; i++)
			if (valor.charAt(i) != valor.charAt(0))
				igual = false;
		if (igual || valor.equals("12345678909"))
			return false;
		int[] numeros = new int[11];
		try {
			for (int i = 0; i < 11; i++)
				numeros[i] = Integer.parseInt(
						Character.toString(valor.charAt(i)));
		} catch (NumberFormatException ex) {
			return false;
		}
		int soma = 0;
		for (int i = 0; i < 9; i++)
			soma += (10 - i) * numeros[i];
		int resultado = soma % 11;
		if (resultado == 1 || resultado == 0) {
			if (numeros[9] != 0)
				return false;
		} else if (numeros[9] != 11 - resultado)
			return false;
		soma = 0;
		for (int i = 0; i < 10; i++)
			soma += (11 - i) * numeros[i];
		resultado = soma % 11;
		if (resultado == 1 || resultado == 0) {
			if (numeros[10] != 0)
				return false;
		} else if (numeros[10] != 11 - resultado)
			return false;
		return true;
	}

	public static boolean validaCnpj(String vrCNPJ) {
		String cnpj = vrCNPJ.replace(".", "");
		cnpj = cnpj.replace("/", "");
		cnpj = cnpj.replace("-", "");
		int[] digitos;
		int[] soma;
		int[] resultado;
		int nrDig;
		String ftmt;
		boolean[] cnpjOk;
		ftmt = "6543298765432";
		digitos = new int[14];
		soma = new int[2];
		soma[0] = 0;
		soma[1] = 0;
		resultado = new int[2];
		resultado[0] = 0;
		resultado[1] = 0;
		cnpjOk = new boolean[2];
		cnpjOk[0] = false;
		cnpjOk[1] = false;
		try {
			for (nrDig = 0; nrDig < 14; nrDig++) {
				digitos[nrDig] = Integer.parseInt(
						cnpj.substring(nrDig, 1));
				if (nrDig <= 11)
					soma[0] += (digitos[nrDig] *
							Integer.parseInt(ftmt.substring(
									nrDig + 1, 1)));
				if (nrDig <= 12)
					soma[1] += (digitos[nrDig] *
							Integer.parseInt(ftmt.substring(
									nrDig, 1)));
			}
			for (nrDig = 0; nrDig < 2; nrDig++) {
				resultado[nrDig] = (soma[nrDig] % 11);
				if ((resultado[nrDig] == 0) || (resultado[nrDig] == 1))
					cnpjOk[nrDig] = (digitos[12 + nrDig] == 0);
				else
					cnpjOk[nrDig] = (digitos[12 + nrDig] == (11 - resultado[nrDig]));
			}
			return (cnpjOk[0] && cnpjOk[1]);
		} catch (Exception ex) {
			return false;
		}
	}
}
