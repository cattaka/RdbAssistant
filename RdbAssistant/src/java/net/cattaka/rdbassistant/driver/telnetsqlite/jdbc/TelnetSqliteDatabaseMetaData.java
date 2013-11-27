/*
 * Copyright (c) 2010, Takao Sumitomo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms,
 * with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *     * Redistributions of source code must retain the
 *       above copyright notice, this list of conditions
 *       and the following disclaimer.
 *     * Redistributions in binary form must reproduce
 *       the above copyright notice, this list of
 *       conditions and the following disclaimer in the
 *       documentation and/or other materials provided
 *       with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software
 * and documentation are those of the authors and should
 * not be interpreted as representing official policies,
 * either expressed or implied.
 */
package net.cattaka.rdbassistant.driver.telnetsqlite.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

import net.cattaka.rdbassistant.driver.telnetsqlite.TelnetSqliteSqlEditorSelection;

public class TelnetSqliteDatabaseMetaData implements DatabaseMetaData {
	private TelnetSqliteConnection connection;


	public TelnetSqliteDatabaseMetaData(TelnetSqliteConnection connection) {
		super();
		this.connection = connection;
	}

	public boolean allProceduresAreCallable() throws SQLException {
		// 不要
		return false;
	}

	public boolean allTablesAreSelectable() throws SQLException {
		// 不要
		return false;
	}

	public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
		// 不要
		return false;
	}

	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
		// 不要
		return false;
	}

	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
		// 不要
		return false;
	}

	public boolean deletesAreDetected(int paramInt) throws SQLException {
		// 不要
		return false;
	}

	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
		// 不要
		return false;
	}

	public ResultSet getAttributes(String paramString1, String paramString2,
			String paramString3, String paramString4) throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getBestRowIdentifier(String paramString1,
			String paramString2, String paramString3, int paramInt,
			boolean paramBoolean) throws SQLException {
		// 不要
		return null;
	}

	public String getCatalogSeparator() throws SQLException {
		// 不要
		return null;
	}

	public String getCatalogTerm() throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getCatalogs() throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getClientInfoProperties() throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getColumnPrivileges(String paramString1,
			String paramString2, String paramString3, String paramString4)
			throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getColumns(String paramString1, String paramString2,
			String paramString3, String paramString4) throws SQLException {

		String sql = TelnetSqliteSqlEditorSelection.SELECT_DETAIL_SQL_LIST[0];
		PreparedStatement stmt = this.connection.prepareStatement(sql);
		return stmt.executeQuery();
	}

	public Connection getConnection() throws SQLException {
		return getConnection();
	}

	public ResultSet getCrossReference(String paramString1,
			String paramString2, String paramString3, String paramString4,
			String paramString5, String paramString6) throws SQLException {
		// 不要
		return null;
	}

	public int getDatabaseMajorVersion() throws SQLException {
		// 不要
		return 0;
	}

	public int getDatabaseMinorVersion() throws SQLException {
		// 不要
		return 0;
	}

	public String getDatabaseProductName() throws SQLException {
		// 不要
		return null;
	}

	public String getDatabaseProductVersion() throws SQLException {
		// 不要
		return null;
	}

	public int getDefaultTransactionIsolation() throws SQLException {
		// 不要
		return 0;
	}

	public int getDriverMajorVersion() {
		// 不要
		return 0;
	}

	public int getDriverMinorVersion() {
		// 不要
		return 0;
	}

	public String getDriverName() throws SQLException {
		// 不要
		return null;
	}

	public String getDriverVersion() throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getExportedKeys(String paramString1, String paramString2,
			String paramString3) throws SQLException {
		// 不要
		return null;
	}

	public String getExtraNameCharacters() throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getFunctionColumns(String paramString1,
			String paramString2, String paramString3, String paramString4)
			throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getFunctions(String paramString1, String paramString2,
			String paramString3) throws SQLException {
		// 不要
		return null;
	}

	public String getIdentifierQuoteString() throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getImportedKeys(String paramString1, String paramString2,
			String paramString3) throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getIndexInfo(String paramString1, String paramString2,
			String paramString3, boolean paramBoolean1, boolean paramBoolean2)
			throws SQLException {
		// 不要
		return null;
	}

	public int getJDBCMajorVersion() throws SQLException {
		// 不要
		return 0;
	}

	public int getJDBCMinorVersion() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxBinaryLiteralLength() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxCatalogNameLength() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxCharLiteralLength() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxColumnNameLength() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxColumnsInGroupBy() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxColumnsInIndex() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxColumnsInOrderBy() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxColumnsInSelect() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxColumnsInTable() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxConnections() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxCursorNameLength() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxIndexLength() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxProcedureNameLength() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxRowSize() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxSchemaNameLength() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxStatementLength() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxStatements() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxTableNameLength() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxTablesInSelect() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxUserNameLength() throws SQLException {
		// 不要
		return 0;
	}

	public String getNumericFunctions() throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getPrimaryKeys(String paramString1, String paramString2,
			String paramString3) throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getProcedureColumns(String paramString1,
			String paramString2, String paramString3, String paramString4)
			throws SQLException {
		// 不要
		return null;
	}

	public String getProcedureTerm() throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getProcedures(String paramString1, String paramString2,
			String paramString3) throws SQLException {
		// 不要
		return null;
	}

	public int getResultSetHoldability() throws SQLException {
		// 不要
		return 0;
	}

	public String getSQLKeywords() throws SQLException {
		// 不要
		return null;
	}

	public int getSQLStateType() throws SQLException {
		// 不要
		return 0;
	}

	public String getSchemaTerm() throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getSchemas() throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getSchemas(String paramString1, String paramString2)
			throws SQLException {
		// 不要
		return null;
	}

	public String getSearchStringEscape() throws SQLException {
		// 不要
		return null;
	}

	public String getStringFunctions() throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getSuperTables(String paramString1, String paramString2,
			String paramString3) throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getSuperTypes(String paramString1, String paramString2,
			String paramString3) throws SQLException {
		// 不要
		return null;
	}

	public String getSystemFunctions() throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getTablePrivileges(String paramString1,
			String paramString2, String paramString3) throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getTableTypes() throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getTables(String paramString1, String paramString2,
			String paramString3, String[] paramArrayOfString)
			throws SQLException {

		String sql = TelnetSqliteSqlEditorSelection.SELECT_TABLES_LIST;
		PreparedStatement stmt = this.connection.prepareStatement(sql);
		return stmt.executeQuery();
	}

	public String getTimeDateFunctions() throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getTypeInfo() throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getUDTs(String paramString1, String paramString2,
			String paramString3, int[] paramArrayOfInt) throws SQLException {
		// 不要
		return null;
	}

	public String getURL() throws SQLException {
		// 不要
		return null;
	}

	public String getUserName() throws SQLException {
		// 不要
		return null;
	}

	public ResultSet getVersionColumns(String paramString1,
			String paramString2, String paramString3) throws SQLException {
		// 不要
		return null;
	}

	public boolean insertsAreDetected(int paramInt) throws SQLException {
		// 不要
		return false;
	}

	public boolean isCatalogAtStart() throws SQLException {
		// 不要
		return false;
	}

	public boolean isReadOnly() throws SQLException {
		// 不要
		return false;
	}

	public boolean locatorsUpdateCopy() throws SQLException {
		// 不要
		return false;
	}

	public boolean nullPlusNonNullIsNull() throws SQLException {
		// 不要
		return false;
	}

	public boolean nullsAreSortedAtEnd() throws SQLException {
		// 不要
		return false;
	}

	public boolean nullsAreSortedAtStart() throws SQLException {
		// 不要
		return false;
	}

	public boolean nullsAreSortedHigh() throws SQLException {
		// 不要
		return false;
	}

	public boolean nullsAreSortedLow() throws SQLException {
		// 不要
		return false;
	}

	public boolean othersDeletesAreVisible(int paramInt) throws SQLException {
		// 不要
		return false;
	}

	public boolean othersInsertsAreVisible(int paramInt) throws SQLException {
		// 不要
		return false;
	}

	public boolean othersUpdatesAreVisible(int paramInt) throws SQLException {
		// 不要
		return false;
	}

	public boolean ownDeletesAreVisible(int paramInt) throws SQLException {
		// 不要
		return false;
	}

	public boolean ownInsertsAreVisible(int paramInt) throws SQLException {
		// 不要
		return false;
	}

	public boolean ownUpdatesAreVisible(int paramInt) throws SQLException {
		// 不要
		return false;
	}

	public boolean storesLowerCaseIdentifiers() throws SQLException {
		// 不要
		return false;
	}

	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
		// 不要
		return false;
	}

	public boolean storesMixedCaseIdentifiers() throws SQLException {
		// 不要
		return false;
	}

	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
		// 不要
		return false;
	}

	public boolean storesUpperCaseIdentifiers() throws SQLException {
		// 不要
		return false;
	}

	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsANSI92EntryLevelSQL() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsANSI92FullSQL() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsANSI92IntermediateSQL() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsAlterTableWithAddColumn() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsAlterTableWithDropColumn() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsBatchUpdates() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsCatalogsInDataManipulation() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsCatalogsInProcedureCalls() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsCatalogsInTableDefinitions() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsColumnAliasing() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsConvert() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsConvert(int paramInt1, int paramInt2)
			throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsCoreSQLGrammar() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsCorrelatedSubqueries() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsDataDefinitionAndDataManipulationTransactions()
			throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsDataManipulationTransactionsOnly()
			throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsDifferentTableCorrelationNames() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsExpressionsInOrderBy() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsExtendedSQLGrammar() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsFullOuterJoins() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsGetGeneratedKeys() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsGroupBy() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsGroupByBeyondSelect() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsGroupByUnrelated() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsIntegrityEnhancementFacility() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsLikeEscapeClause() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsLimitedOuterJoins() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsMinimumSQLGrammar() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsMixedCaseIdentifiers() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsMultipleOpenResults() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsMultipleResultSets() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsMultipleTransactions() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsNamedParameters() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsNonNullableColumns() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsOrderByUnrelated() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsOuterJoins() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsPositionedDelete() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsPositionedUpdate() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsResultSetConcurrency(int paramInt1, int paramInt2)
			throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsResultSetHoldability(int paramInt)
			throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsResultSetType(int paramInt) throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsSavepoints() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsSchemasInDataManipulation() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsSchemasInIndexDefinitions() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsSchemasInProcedureCalls() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsSchemasInTableDefinitions() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsSelectForUpdate() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsStatementPooling() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsStoredProcedures() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsSubqueriesInComparisons() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsSubqueriesInExists() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsSubqueriesInIns() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsSubqueriesInQuantifieds() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsTableCorrelationNames() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsTransactionIsolationLevel(int paramInt)
			throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsTransactions() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsUnion() throws SQLException {
		// 不要
		return false;
	}

	public boolean supportsUnionAll() throws SQLException {
		// 不要
		return false;
	}

	public boolean updatesAreDetected(int paramInt) throws SQLException {
		// 不要
		return false;
	}

	public boolean usesLocalFilePerTable() throws SQLException {
		// 不要
		return false;
	}

	public boolean usesLocalFiles() throws SQLException {
		// 不要
		return false;
	}

	public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
		// 不要
		return false;
	}

	public <T> T unwrap(Class<T> paramClass) throws SQLException {
		// 不要
		return null;
	}

	public boolean generatedKeyAlwaysReturned() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public ResultSet getPseudoColumns(String arg0, String arg1, String arg2,
			String arg3) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public RowIdLifetime getRowIdLifetime() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


}
