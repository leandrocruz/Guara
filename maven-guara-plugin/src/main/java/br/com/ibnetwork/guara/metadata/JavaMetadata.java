package br.com.ibnetwork.guara.metadata;

public interface JavaMetadata
    extends Metadata
{
	String getCompilationUnit();
	
	String getPackageName();
	
	String getBaseClass();
}
