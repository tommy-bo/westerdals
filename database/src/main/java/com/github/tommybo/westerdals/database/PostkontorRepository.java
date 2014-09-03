package com.github.tommybo.westerdals.database;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;

public interface PostkontorRepository {
	public Optional<Postkontor> hentPostkontorForPostnummer(String postnummer);
	public ImmutableSet<Postkontor> hentAllePostkontor();
}
