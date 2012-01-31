package br.com.ibnetwork.guara.metadata.handler;

import java.io.File;

import br.com.ibnetwork.guara.metadata.Metadata;

public interface FileHandler
{

	void handleFile(File file, String text, Metadata meta)
		throws Exception;

}
