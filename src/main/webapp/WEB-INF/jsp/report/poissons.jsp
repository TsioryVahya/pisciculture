<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="fr" class="h-full bg-white">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="h-full overflow-hidden flex">
    <!-- Sidebar Component -->
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <!-- Main Content -->
    <div class="flex-1 flex flex-col min-w-0 overflow-hidden">
        <!-- Top Navigation -->
        <header class="bg-white border-b border-gray-200">
            <div class="px-4 sm:px-6 lg:px-8">
                <div class="flex justify-between h-16 items-center">
                    <h1 class="text-lg font-semibold text-gray-900">${title}</h1>
                </div>
            </div>
        </header>

        <!-- Page Content -->
        <main class="flex-1 overflow-y-auto bg-gray-50 p-6">
            <div class="max-w-7xl mx-auto">
                <div class="mb-6">
                    <h2 class="text-2xl font-bold text-gray-900">Reporting des Poissons</h2>
                    <p class="mt-1 text-sm text-gray-600">
                        Coût initial, coût d'alimentation cumulé, revenu théorique à poids maximal et bénéfice estimé par poisson.
                    </p>
                </div>

                <div class="bg-white shadow overflow-hidden sm:rounded-lg border border-gray-200">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Poisson</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Race</th>
                                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Poids Initial (kg)</th>
                                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Poids Atteint (kg)</th>
                                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Coût Initial (Ar)</th>
                                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Coût Alimentation (Ar)</th>
                                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Revenu Théorique (Ar)</th>
                                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Bénéfice (Ar)</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <c:forEach items="${rows}" var="row">
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                        <a href="${pageContext.request.contextPath}/poissons/history/${row.poisson.id}" class="text-emerald-600 hover:text-emerald-900">
                                            ${row.poisson.nom}
                                        </a>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        ${row.race.nom}
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-right text-gray-700">
                                        <fmt:formatNumber value="${row.poidsInitial}" pattern="#,##0.000000" />
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-right text-gray-700">
                                        <fmt:formatNumber value="${row.poidsAtteint}" pattern="#,##0.000000" />
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-right text-gray-700">
                                        <fmt:formatNumber value="${row.coutInitial}" pattern="#,##0" /> Ar
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-right text-gray-700">
                                        <fmt:formatNumber value="${row.coutAlimentation}" pattern="#,##0" /> Ar
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-right text-green-700 font-medium">
                                        <fmt:formatNumber value="${row.revenu}" pattern="#,##0" /> Ar
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-right">
                                        <c:choose>
                                            <c:when test="${row.benefice >= 0}">
                                                <span class="text-emerald-700 font-semibold">
                                                    <fmt:formatNumber value="${row.benefice}" pattern="#,##0" /> Ar
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-red-600 font-semibold">
                                                    <fmt:formatNumber value="${row.benefice}" pattern="#,##0" /> Ar
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty rows}">
                                <tr>
                                    <td colspan="8" class="px-6 py-10 text-center text-sm text-gray-500 italic">
                                        Aucun poisson n'est disponible pour le reporting.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
