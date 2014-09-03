package com.github.tommybo.westerdals.database;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

public class Postkontor {

	private final String navn;
	private final ImmutableSet<String> postnummer;

	private Postkontor(String navn, ImmutableSet<String> postnummer) {
		this.navn = navn;
		this.postnummer = postnummer;
	}

	public String getNavn() {
		return navn;
	}

	public Set<String> getPostnummer() {
		return postnummer;
	}

	public static Builder med() {
		return new Builder();
	}

	private static class Builder {

		private String navn;
		private ImmutableSet<String> postnummer;

		private Builder() {
		}

		public Builder navn(String navn) {
			this.navn = navn;
			return this;
		}

		public Builder postnummer(Set<String> postnummer) {
			this.postnummer = ImmutableSet.copyOf(postnummer);
			return this;
		}

		public Postkontor build() {
			return new Postkontor(navn, postnummer);
		}
	}
}
