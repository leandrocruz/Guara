package br.com.ibnetwork.guara.generator;

import br.com.ibnetwork.guara.metadata.Metadata;

public interface Generator
{
	String generate(Metadata meta);
}
