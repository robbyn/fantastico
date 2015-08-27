<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="country">insert into countries(CODE) values('<xsl:value-of select="@code"/>');<xsl:apply-templates/></xsl:template>
<xsl:template match="title">insert into countries_labels(LANGUAGE_CODE,LANGUAGE,LABEL) values('<xsl:value-of select="../@code"/>','<xsl:value-of select="@language"/>','<xsl:value-of select="text()"/>');</xsl:template>
</xsl:stylesheet>
