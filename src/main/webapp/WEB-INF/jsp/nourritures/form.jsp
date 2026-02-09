<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr" class="h-full bg-white">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="h-full overflow-hidden flex">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="flex-1 flex flex-col min-w-0 overflow-hidden">
        <header class="bg-white border-b border-gray-200">
            <div class="px-4 sm:px-6 lg:px-8">
                <div class="flex justify-between h-16 items-center">
                    <h1 class="text-lg font-semibold text-gray-900">${title}</h1>
                </div>
            </div>
        </header>

        <main class="flex-1 overflow-y-auto bg-gray-50 p-6">
            <div class="max-w-3xl mx-auto">
                <div class="bg-white shadow sm:rounded-lg border border-gray-200 p-6">
                    <form action="${pageContext.request.contextPath}/nourritures/save" method="POST" class="space-y-6">
                        <input type="hidden" name="id" value="${nourriture.id}">
                        
                        <div>
                            <label for="nom" class="block text-sm font-medium text-gray-700">Nom de la nourriture</label>
                            <input type="text" name="nom" id="nom" value="${nourriture.nom}" required class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm">
                        </div>

                        <div class="grid grid-cols-1 gap-y-6 gap-x-4 sm:grid-cols-2">
                            <div>
                                <label for="prixAchatParKg" class="block text-sm font-medium text-gray-700">Prix d'achat (par kg)</label>
                                <input type="number" step="0.01" name="prixAchatParKg" id="prixAchatParKg" value="${nourriture.prixAchatParKg}" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm">
                            </div>

                        </div>

                        <!-- Nutriments Modulables -->
                        <div class="border-t border-gray-200 pt-6">
                            <h3 class="text-lg font-medium text-gray-900 mb-4">Apports Nutritionnels Modulables</h3>
                            <div class="grid grid-cols-1 gap-y-6 gap-x-4 sm:grid-cols-2">
                                <c:forEach items="${nutriments}" var="n">
                                    <div>
                                        <label for="nutriment_${n.id}" class="block text-sm font-medium text-gray-700">Apport en ${n.nom} (%)</label>
                                        <input type="number" step="0.01" name="nutriment_${n.id}" id="nutriment_${n.id}" 
                                               value="${nutrientValues[n.id]}" 
                                               class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm">
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="flex justify-end space-x-3">
                            <a href="${pageContext.request.contextPath}/nourritures" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500">
                                Annuler
                            </a>
                            <button type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-emerald-600 hover:bg-emerald-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500">
                                Enregistrer
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
